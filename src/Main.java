import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Main {
    private static final int MAX_GUESSES = 7;
    private static final String WORDLIST_FILENAME = "wordlist.txt";
    private static ArrayList<String> targetWords = new ArrayList<>();
    private static String targetWord;
    private static int remainingGuesses;
    private static Set<Character> guessedLetters = new HashSet<>();

    public static void main(String[] args) throws FileNotFoundException {
        loadWordList();
        selectTargetWord();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void loadWordList() throws FileNotFoundException {
        Scanner in = new Scanner(new File(WORDLIST_FILENAME));
        while (in.hasNext()) {
            String word = in.next();
            if (word.length() >= 7 && word.length() <= 10) {
                targetWords.add(word);
            }
        }
        in.close();
    }

    private static void selectTargetWord() {
        Random r = new Random();
        targetWord = targetWords.get(r.nextInt(targetWords.size()));
        remainingGuesses = MAX_GUESSES;
        guessedLetters.clear();
        System.out.println("The target word is: " + targetWord);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Hangman Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel wordLabel = new JLabel(createHiddenWordString());
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        JPanel wordPanel = new JPanel();
        wordPanel.add(wordLabel);
        JLabel remainingLabel = new JLabel("Guesses remaining: " + remainingGuesses);
        remainingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JPanel remainingPanel = new JPanel();
        remainingPanel.add(remainingLabel);
        JTextField inputField = new JTextField(1);
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText = inputField.getText();
                if (inputText.length() == 1) {
                    char inputChar = inputText.charAt(0);
                    if (Character.isLetter(inputChar)) {
                        inputChar = Character.toLowerCase(inputChar);
                        if (guessedLetters.contains(inputChar)) {
                            JOptionPane.showMessageDialog(frame, "You already guessed this letter.");
                        } else {
                            guessedLetters.add(inputChar);
                            if (targetWord.indexOf(inputChar) == -1) {
                                remainingGuesses--;
                                if (remainingGuesses == 0) {
                                    JOptionPane.showMessageDialog(frame, "You lose. The word was " + targetWord + ".");
                                    selectTargetWord();
                                    wordLabel.setText(createHiddenWordString());
                                    remainingLabel.setText("Guesses remaining: " + remainingGuesses);
                                    inputField.requestFocusInWindow();
                                } else {
                                    remainingLabel.setText("Guesses remaining: " + remainingGuesses);
                                }
                            } else {
                                wordLabel.setText(createHiddenWordString());
                                if (targetWord.equals(getCurrentWord())) {
                                    JOptionPane.showMessageDialog(frame, "You win!");
                                    selectTargetWord();
                                    wordLabel.setText(createHiddenWordString());
                                    remainingLabel.setText("Guesses remaining: " + remainingGuesses);
                                    inputField.requestFocusInWindow();
                                }
                            }
                        }
                        inputField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please enter a letter.");
                        inputField.setText("");
                    }
                }
            }
        });
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Guess a letter: "));
        inputPanel.add(inputField);
        inputPanel.add(Box.createHorizontalStrut(10));
        inputPanel.add(inputField);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(wordPanel);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(remainingPanel);
        frame.getContentPane().add(Box.createVerticalStrut(10));
        frame.getContentPane().add(inputPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static String createHiddenWordString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < targetWord.length(); i++) {
            char c = targetWord.charAt(i);
            if (guessedLetters.contains(c)) {
                sb.append(c);
            } else {
                sb.append("_");
            }
            sb.append(" ");
        }
        return sb.toString();
    }

    private static String getCurrentWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < targetWord.length(); i++) {
            char c = targetWord.charAt(i);
            if (guessedLetters.contains(c)) {
                sb.append(c);
            } else {
                sb.append("_");
            }
        }
        return sb.toString();
    }
}
