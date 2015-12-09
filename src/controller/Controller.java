package controller;

import SDK.SDKController;
import SDK.dto.Game;
import SDK.dto.Gamer;
import SDK.dto.Score;
import SDK.dto.User;
import presentation.frame.Screen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller {
    //Declaration
    private Screen screen;
    private SDKController sdkController;
    private User currentUser;
    private ArrayList<User> users;
    private ArrayList<Game> games;
    private ArrayList<Score> scores;

    public Controller() {
        screen = new Screen();
        screen.setVisible(true);

        sdkController = new SDKController();
        currentUser = new User();
    }

    //Method runs the program. Executed from main
    public void run() {
        screen.show(Screen.LOGINMENU);
        screen.getLoginMenu().addActionListener(new LoginActionListener());
        screen.getCreateGame().addActionListener(new CreateGameActionListener());
        screen.getDeleteGame().addActionListener(new DeleteGameActionListener());
        screen.getHighscore().addActionListener(new HighscoreActionListener());
        screen.getUserMenu().addActionListener(new UserMenuActionListener());
        screen.getJoinGame().addActionListener(new JoinGameActionListener());
    }

    private class LoginActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == screen.getLoginMenu().getBtnLogin()) {

                try {
                    //Collects input typed by currentUser and sends it to sdkController.login
                    currentUser.setUsername(screen.getLoginMenu().getTxtUserName().getText());
                    currentUser.setPassword(screen.getLoginMenu().getTxtPassword().getText());
                    String loginMessage = sdkController.login(currentUser);

                    //Checks if the return message equals "Login successful"
                    if (loginMessage.equals("Login successful")) {
                        screen.show(Screen.USERMENU);
                        screen.getLoginMenu().getTxtPassword().setText("");
                        screen.getLoginMenu().getTxtUserName().setText("");
                    }
                    //JOptionPane shows the messages from the server
                    else {
                        JOptionPane.showMessageDialog(screen, loginMessage, "Invalid input", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NullPointerException e1) {
                    JOptionPane.showMessageDialog(screen, "Missing username and password. Try again", "Missing input", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class UserMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == screen.getUserMenu().getBtnJoinGame()) {
                //setting games in ComboBox in panel JoinGame
                games = sdkController.getGames(currentUser.getId());
                screen.getJoinGame().AddGamesInComboBox(games);
                screen.show(Screen.JOINGAME);
            } else if (e.getSource() == screen.getUserMenu().getBtnDeleteGame()) {
                //setting games in ComboBox in panel DeleteGame
                games = sdkController.getGames(currentUser.getId());
                screen.getDeleteGame().AddGamesInComboBox(games);
                screen.show(Screen.DELETEGAME);
            } else if (e.getSource() == screen.getUserMenu().getBtnCreateGame()) {
                //setting users in ComboBox in panel CreateGame
                users = sdkController.getUsers();
                screen.getCreateGame().AddUserInComboBox(users);
                screen.show(Screen.CREATEGAME);
            } else if (e.getSource() == screen.getUserMenu().getBtnHighscore()) {
                //setting scores in JTable in panel Highscore
                scores = sdkController.getHighScores();
                screen.getHighscore().setTableShowHighscore(scores);
                screen.show(Screen.HIGHSCORE);
            } else if (e.getSource() == screen.getUserMenu().getBtnLogOff()) {
                //clearing inputFields in panel LoginMenu
                screen.getLoginMenu().getTxtPassword().setText("");
                screen.getLoginMenu().getTxtUserName().setText("");
                screen.show(Screen.LOGINMENU);
            }
        }
    }

    private class CreateGameActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == screen.getCreateGame().getBtnBack()) {
                    //clearing inputFields
                    screen.getCreateGame().getTxtControls().setText("");
                    screen.getCreateGame().getTxtGameName().setText("");
                    screen.show((Screen.USERMENU));
                } else if (e.getSource() == screen.getCreateGame().getBtnCreateGame()) {
                    //new object of Gamer named opponent
                    Gamer opponent = new Gamer();
                    //Loops through ArrayList users
                    for (User user : users) {
                        if (user.getUsername() == screen.getCreateGame().getChosenUser()) {
                            opponent.setId(user.getId());
                            //Making sure host cannot challenge himself
                            if (opponent.getId() == currentUser.getId()) {
                                JOptionPane.showMessageDialog(screen, "YOU CAN'T CHOOSE YOURSELF AS OPPONENT.");
                            } else {
                                //new object of Gamer named host
                                Gamer host = new Gamer();

                                //adding content to host
                                host.setId(currentUser.getId());
                                host.setControls(screen.getCreateGame().getTxtControls().getText());

                                //new object of Game named createGame
                                Game createGame = new Game();

                                //adding content to createGame
                                createGame.setName(screen.getCreateGame().getTxtGameName().getText());
                                createGame.setMapSize((Integer) screen.getCreateGame().getComboBoxMapSize().getSelectedItem());
                                createGame.setHost(host);
                                createGame.setOpponent(opponent);

                                //sending data to sdkController.createGame
                                Object createGameMsg = sdkController.createGame(createGame);

                                JOptionPane.showMessageDialog(screen, createGameMsg, "Game created", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog(screen, "Controls needs to be W,A,S,D", "Create game failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class HighscoreActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == screen.getHighscore().getBtnBack()) {
                screen.show(Screen.USERMENU);
            }
        }
    }

    private class DeleteGameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == screen.getDeleteGame().getBtnBack()) {
                screen.show((Screen.USERMENU));
            } else if (e.getSource() == screen.getDeleteGame().getBtnDeleteGame()) {
                //New object of Game
                Game deleteGame = new Game();
                deleteGame.setName(screen.getDeleteGame().getChosenGame());
                //Loops through ArrayList games
                for (Game game : games) {
                    if (game.getName() == screen.getDeleteGame().getChosenGame()) {
                        deleteGame = game;
                    }
                }
                //Sending data to sdkController.deleteGame
                Object deleteGameMsg = sdkController.deleteGame(deleteGame.getGameId());

                JOptionPane.showMessageDialog(screen, deleteGameMsg);
            }

        }
    }

    private class JoinGameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == screen.getJoinGame().getBtnBack()) {
                //clearing inputFields
                screen.getJoinGame().getTxtControl().setText("");

                screen.show(Screen.USERMENU);
            } else if (e.getSource() == screen.getJoinGame().getBtnJoinGame()) {
                //New object of game named joinGame
                Game joinGame = new Game();

                //Loops through ArrayList games
                for (Game game : games) {
                    if (game.getName().equals(screen.getJoinGame().getChosenGame())) {
                        joinGame = game;
                    }
                }
                //New object of Gamer named opponent
                Gamer opponent = new Gamer();
                //Adding controls to opponent and sets opponent in joinGame
                opponent.setControls(screen.getJoinGame().getTxtControl().getText());
                joinGame.setOpponent(opponent);

                //Sending data to sckController.joinGame and sdkController.executeGame
                Object joinGameMessage = sdkController.joinGame(joinGame);
                Object startGameMessage = sdkController.executeGame(joinGame);

                String winner = null;
                //Loops through ArrayList users
                for (User user : users) {
                    try {
                        if (user.getId() == Integer.parseInt(String.valueOf(startGameMessage))) {
                            winner = user.getUsername();
                        }
                    } catch (NumberFormatException e1) {
                        e1.printStackTrace();
                    }
                }
                JOptionPane.showMessageDialog(screen, joinGameMessage + " .. AND THE WINNER IS: " + winner);
            }
        }
    }
}