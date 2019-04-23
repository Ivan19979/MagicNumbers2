package com.magic;

import com.magic.Question.OperationType;
import com.magic.Question.Question;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionsPanel extends JPanel {

    private int currLives;
    private int currQuestions;
    private int maxQuestion;
    private int score = 0;
    private Question currentQuestion;
    private List<OperationType> answers = new ArrayList<OperationType>();

    private static String livesStr = "Жизней: ";
    private static String questionStr = "Вопросов: ";

    private JPanel upperBound;
    private JLabel livesLabel;
    private JLabel questionsLabel;

    private JTextArea questionText;
    private JPanel questionPanel;

    private JButton plusBtn;
    private JButton minusBtn;
    private JButton multiplyBtn;
    private JButton resetBtn;
    private JButton nextBtn;
    private JPanel buttonsPanel;

    public QuestionsPanel(int lives, int count, int deep) {
        Font font = new Font("serif", Font.PLAIN, Main.fontSize);
        this.currLives = lives;
        this.maxQuestion = count;
        this.currQuestions = 1;
        currentQuestion = new Question(deep);
        currentQuestion.generateQuestion();
        upperBound = new JPanel(new BorderLayout());
        livesLabel = new JLabel(livesStr + Integer.toString(lives));
        questionsLabel = new JLabel(questionStr + Integer.toString(currQuestions) + "/" + Integer.toString(count));

        livesLabel.setFont(font);
        questionsLabel.setFont(font);

        upperBound.add(livesLabel, BorderLayout.LINE_START);
        upperBound.add(questionsLabel, BorderLayout.LINE_END);

        questionText = new JTextArea(currentQuestion.getQuestionString());
        questionText.setEnabled(false);
        questionText.setDisabledTextColor(Color.BLACK);
        questionText.setFont(font);

        questionPanel = new JPanel();
        questionPanel.add(questionText);

        plusBtn = new JButton("+");
        minusBtn = new JButton("-");
        multiplyBtn = new JButton("*");
        resetBtn = new JButton("Сброс");
        nextBtn = new JButton("Дальше");

        plusBtn.addActionListener(e -> {
            this.plus();
        });
        minusBtn.addActionListener(e -> {
            this.minus();
        });
        multiplyBtn.addActionListener(e -> {
            this.multiply();
        });
        resetBtn.addActionListener(e -> {
            this.reset();
        });
        nextBtn.addActionListener(e -> {
            this.nextQuestion();
        });
        buttonsPanel = new JPanel();
        buttonsPanel.add(minusBtn);
        buttonsPanel.add(plusBtn);
        buttonsPanel.add(multiplyBtn);
        buttonsPanel.add(resetBtn);
        buttonsPanel.add(nextBtn);
        this.setLayout(new BorderLayout());
        this.add(upperBound, BorderLayout.NORTH);
        this.add(questionPanel, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void nextQuestion(){
        if(!currentQuestion.checkAnswers(answers)){
            currLives--;
            if(currLives == 0){
                this.endGame();
            }
            livesLabel.setText(livesStr + Integer.toString(currLives));
        } else {
            score++;
        }
        if(currQuestions == maxQuestion){
            this.endGame();
        }
        currQuestions++;
        questionsLabel.setText(questionStr + Integer.toString(currQuestions) + "/" + Integer.toString(maxQuestion));
        answers.clear();
        questionText.setText(currentQuestion.generateQuestion());
        checkCountOfAnswersAndToggleButtons();
    }

    private void reset(){
        answers.clear();
        questionText.setText(currentQuestion.getQuestionWithAnswers(answers));
        checkCountOfAnswersAndToggleButtons();
    }

    private void plus(){
        answers.add(OperationType.PLUS);
        questionText.setText(currentQuestion.getQuestionWithAnswers(answers));
        checkCountOfAnswersAndToggleButtons();
    }

    private void minus(){
        answers.add(OperationType.MINUS);
        questionText.setText(currentQuestion.getQuestionWithAnswers(answers));
        checkCountOfAnswersAndToggleButtons();
    }

    private void multiply(){
        answers.add(OperationType.MULTIPLY);
        questionText.setText(currentQuestion.getQuestionWithAnswers(answers));
        checkCountOfAnswersAndToggleButtons();
    }

    private void checkCountOfAnswersAndToggleButtons(){
        if(answers.size() == currentQuestion.getDeep()) {
            plusBtn.setEnabled(false);
            minusBtn.setEnabled(false);
            multiplyBtn.setEnabled(false);
        } else {
            plusBtn.setEnabled(true);
            minusBtn.setEnabled(true);
            multiplyBtn.setEnabled(true);
        }
    }

    private void endGame() {
        JFrame mainFrame = (JFrame) SwingUtilities.windowForComponent(this);
        mainFrame.remove(this);
        boolean isLose = currLives == 0;
        mainFrame.add(new EndPanel(isLose, score, maxQuestion));
        mainFrame.validate();
    }
}