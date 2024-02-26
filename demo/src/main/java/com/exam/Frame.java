package com.exam;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Frame extends JFrame
{
    static MongoDatabase mongoDatabase;

    // mongoDatabase = DatabaseGeneral.mongoDatabase;
    User user;
    static Result result;

    static Map<String, ArrayList<String>> questionHashMap = new HashMap<String, ArrayList<String>>();

    static JPanel mainContentPanel; // main page
    static JPanel examContentPanel; // exam page
    static JPanel finalResultPanel; // result page

    JPanel mainBtnPanel; // main button page
    JPanel examBtnPanel; // exam button page

    JTextField nameTextField;
    JTextField emailTextField; 

    String userName;
    String userEmail;
    int userIsNew; // enable new email each time
    Float userScore = (float) 0;

    JLabel titleLabel;

    JLabel questionLabel;
    String questionDisplay;

    // A, B, C, D radio buttons
    Box choicesBox = new Box(BoxLayout.Y_AXIS); // ONLY one radio button is selected each time
    ButtonGroup choicesRadioGroup = new ButtonGroup();
    String choiceADisplay;
    JRadioButton aRadioBtn = new JRadioButton("A");
    String choiceBDisplay;
    JRadioButton bRadioBtn = new JRadioButton("B");
    String choiceCDisplay;
    JRadioButton cRadioBtn = new JRadioButton("C");
    String choiceDDisplay;
    JRadioButton dRadioBtn = new JRadioButton("D");
    
    // selected choice for each question
    Box filledChoiceBox = new Box(BoxLayout.Y_AXIS);

    // exam button page style
    FlowLayout btnLayout = new FlowLayout();
    JButton btnNext;
    JButton btnPrevious;
    JButton btnSubmit;

    // result page elements
    JLabel finalResultLabel;
    String finalResultDisplay;

    // fonts
    final private Font titleFont = new Font("Segoe print", Font.BOLD, 36);
    final private Font simpleFont = new Font("Segoe print", Font.BOLD, 24);
    final private Font questionFont = new Font("Segoe print", Font.BOLD, 20);
    final private Font textFieldFont = new Font("Segoe print", Font.ITALIC, 32);
    final private Font btnFont = new Font("Segoe print", Font.PLAIN, 16);
    final private Font finalResultFont = new Font("Segoe print", Font.PLAIN, 48);

    int currentQuestionIndex = 0; // which question you are answering
    static int totalQuestionIndex;

    public void initialize() {
        /***** Title Panel START *****/
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setPreferredSize(new Dimension(800, 100));
        titlePanel.setBorder(new EmptyBorder(25, 0, 4, 4));
        FlowLayout titleLayout = new FlowLayout();
        titlePanel.setLayout(titleLayout);

        // Title Label
        titleLabel = new JLabel("Examinmation Simulator");
        titleLabel.setFont(titleFont);

        titlePanel.setBackground(new Color(180, 220, 255));
        titlePanel.add(titleLabel);
        /***** Title Panel END *****/

        /***** Exam Panel START *****/
        examContentPanel = new JPanel();
        examContentPanel.setBackground(new Color(180, 220, 255));
        examContentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Question Label
        questionDisplay = "Question " + (currentQuestionIndex + 1) + ": " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(1);
        questionLabel = new JLabel(questionDisplay);
        questionLabel.setFont(questionFont);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.9;
        c.gridx = 0;
        c.gridy = 0;
        examContentPanel.add(questionLabel, c);

        // Radio Button Choices
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.9;
        c.gridx = 0;
        c.gridy = 1;
        choiceADisplay = "A. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(2);
        aRadioBtn.setText(choiceADisplay);
        aRadioBtn.setFont(simpleFont);
        aRadioBtn.setBackground(new Color(180, 220, 255));
        aRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                questionHashMap.get(Integer.toString(currentQuestionIndex)).set(questionHashMap.get(Integer.toString(currentQuestionIndex)).size() -1, aRadioBtn.getText().substring(0, 1));
                updateFilledChoiceDisplay();
            }
            
        });
        choicesRadioGroup.add(aRadioBtn);
        choicesBox.add(aRadioBtn);

        choiceBDisplay = "B. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(3);
        bRadioBtn.setText(choiceBDisplay);
        bRadioBtn.setFont(simpleFont);
        bRadioBtn.setBackground(new Color(180, 220, 255));
        bRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                questionHashMap.get(Integer.toString(currentQuestionIndex)).set(questionHashMap.get(Integer.toString(currentQuestionIndex)).size() -1, bRadioBtn.getText().substring(0, 1));
                updateFilledChoiceDisplay();
            }
            
        });
        choicesRadioGroup.add(bRadioBtn);
        choicesBox.add(bRadioBtn);

        choiceCDisplay = "C. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(4);
        cRadioBtn.setText(choiceCDisplay);
        cRadioBtn.setFont(simpleFont);
        cRadioBtn.setBackground(new Color(180, 220, 255));
        cRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                questionHashMap.get(Integer.toString(currentQuestionIndex)).set(questionHashMap.get(Integer.toString(currentQuestionIndex)).size() -1, cRadioBtn.getText().substring(0, 1));
                updateFilledChoiceDisplay();
            }
            
        });
        choicesRadioGroup.add(cRadioBtn);
        choicesBox.add(cRadioBtn);

        choiceDDisplay = "D. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(5);
        dRadioBtn.setText(choiceDDisplay);
        dRadioBtn.setFont(simpleFont);
        dRadioBtn.setBackground(new Color(180, 220, 255));
        dRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                questionHashMap.get(Integer.toString(currentQuestionIndex)).set(questionHashMap.get(Integer.toString(currentQuestionIndex)).size() -1, dRadioBtn.getText().substring(0, 1));
                updateFilledChoiceDisplay();
            }
            
        });
        choicesRadioGroup.add(dRadioBtn);
        choicesBox.add(dRadioBtn);

        examContentPanel.add(choicesBox, c);

        updateFilledChoiceDisplay();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.gridheight = 2;
        c.ipady = 300;
        c.gridx = 1;
        c.gridy = 0;
        examContentPanel.add(filledChoiceBox, c);
        /***** Exam Panel END *****/

        /***** Main Panel START *****/
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new GridLayout(3, 2, -400, 50));

        // Name Lavel
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(simpleFont);

        // Email Label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(simpleFont);

        // Name Text Field
        nameTextField = new JTextField();
        nameTextField.setFont(textFieldFont);

        // Email Text Field
        emailTextField = new JTextField();
        emailTextField.setFont(textFieldFont);

        mainContentPanel.setBackground(new Color(180, 220, 255));
        mainContentPanel.add(nameLabel);
        mainContentPanel.add(nameTextField);
        mainContentPanel.add(emailLabel);
        mainContentPanel.add(emailTextField);
        /***** Main Panel END *****/

        /***** Result Panel START *****/
        finalResultPanel = new JPanel();
        finalResultPanel.setBackground(new Color(180, 220, 255));

        finalResultDisplay = "You get " + Integer.toString((int) Math.round(userScore)) + " score";
        finalResultLabel = new JLabel(finalResultDisplay);
        finalResultLabel.setFont(finalResultFont);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.9;
        c.gridx = 0;
        c.gridy = 0;
        finalResultPanel.add(finalResultLabel, c);
        /***** Result Panel END *****/

        /***** Exam Button Panel START *****/
        examBtnPanel = new JPanel(new GridBagLayout());
        examBtnPanel.setPreferredSize(new Dimension(800, 100));
        examBtnPanel.setBorder(new EmptyBorder(50, 0, 4, 4));
        examBtnPanel.setLayout(btnLayout);

        // Previous Button
        btnPrevious = new JButton("PREVIOUS");
        btnPrevious.setFont(btnFont);
        btnPrevious.setEnabled(false);
        btnPrevious.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                switchQuestion("previous");
            }
        });

        // Next Button
        btnNext = new JButton("NEXT");
        btnNext.setFont(btnFont);
        btnNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                switchQuestion("next");
            }
        });

        // Submit Button
        btnSubmit = new JButton("SUBMIT");
        btnSubmit.setFont(btnFont);
        btnSubmit.setBackground(new Color(255, 0, 0));
        btnSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                sumbitAnswers();
            }
            
        });

        examBtnPanel.setBackground(new Color(180, 220, 255));
        examBtnPanel.add(btnPrevious);
        examBtnPanel.add(btnNext);
        examBtnPanel.add(btnSubmit);
        /***** Exam Button Panel END *****/

        /***** Main Button Panel START *****/
        mainBtnPanel = new JPanel(new GridBagLayout());
        mainBtnPanel.setPreferredSize(new Dimension(800, 100));
        mainBtnPanel.setBorder(new EmptyBorder(50, 0, 4, 4));
        mainBtnPanel.setLayout(btnLayout);

        // Start Button
        JButton btnStart = new JButton("START");
        btnStart.setFont(btnFont);
        btnStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                userName = nameTextField.getText();
                userEmail = emailTextField.getText();
                
                user = new User(mongoDatabase, userName, userEmail);
                userIsNew = user.createUser();

                if (userName.length() == 0 || userEmail.length() == 0 || userIsNew == 0) {
                    System.out.println("try another email");
                } else {
                    startExam();
                }
            }
        });

        mainBtnPanel.setBackground(new Color(180, 220, 255));
        mainBtnPanel.add(btnStart);
        /***** Main Button Panel END *****/

        /***** First Page Panels START *****/
        add(titlePanel, "North");
        add(mainContentPanel, "Center");
        // add(examContentPanel, "Center");
        // add(finalResultPanel, "Center");
        add(mainBtnPanel, "South");
        // add(examBtnPanel, "South");
        /***** First Page Panels END *****/

        setTitle("Exam Simulator");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // click 'start' button
    public void startExam() {
        remove(mainBtnPanel);
        remove(mainContentPanel);
        add(examContentPanel, "Center");
        titleLabel.setText("Hi " + userName);
        add(examBtnPanel, "South");
        revalidate();
        repaint();
    }

    // click 'previous' or 'next' button
    public void switchQuestion(String option) {
        if (option == "next") {
            if (currentQuestionIndex >= 0 && currentQuestionIndex < totalQuestionIndex - 1) 
                currentQuestionIndex++;
        } else {
            if (currentQuestionIndex > 0 && currentQuestionIndex < totalQuestionIndex) 
                currentQuestionIndex--;
        }

        if (currentQuestionIndex == 0) {
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(true);
        }
        else if (currentQuestionIndex < totalQuestionIndex -1 ) {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
        }
        else {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(false);
        }

        choicesRadioGroup.clearSelection();
        questionDisplay = "Question " + (currentQuestionIndex + 1) + ": " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(1);
        questionLabel.setText(questionDisplay);

        choiceADisplay = "A. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(2);
        aRadioBtn.setText(choiceADisplay);
        choiceBDisplay = "B. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(3);
        bRadioBtn.setText(choiceBDisplay);
        choiceCDisplay = "C. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(4);
        cRadioBtn.setText(choiceCDisplay);
        choiceDDisplay = "D. " + questionHashMap.get(Integer.toString(currentQuestionIndex)).get(5);
        dRadioBtn.setText(choiceDDisplay);
    }

    // update selected choices
    public void updateFilledChoiceDisplay() {
        filledChoiceBox.removeAll();

        for (int i = 0; i < totalQuestionIndex; i++) {
            String filledChoiceBoxRow = Integer.toString(i + 1) + ". " +  questionHashMap.get(Integer.toString(i)).get(6);
            JLabel filledChoiceBoxRowLabel = new JLabel(filledChoiceBoxRow);
            filledChoiceBox.add(filledChoiceBoxRowLabel);
        }
    }

    // click 'sumbit' button
    public void sumbitAnswers() {
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);
        btnSubmit.setEnabled(false);
        remove(examContentPanel);
        remove(mainBtnPanel);
        add(finalResultPanel, "Center");
        revalidate();
        repaint();
        result.saveUser(userEmail);
        userScore = result.checkSubmit(userEmail, questionHashMap);
        finalResultDisplay = "You get " + Integer.toString((int) Math.round(userScore)) + " score";
        finalResultLabel.setText(finalResultDisplay);
        
    }

    public static void main( String[] args )
    {
        // mongodb
        DatabaseGeneral mongoDBGeneral = new DatabaseGeneral("127.0.0.1", 27017); // localhost:27017
        mongoDBGeneral.checkServiceConnection(); // connected to datalake
        mongoDBGeneral.checkDatabaseConnection(); // connected to database

        mongoDBGeneral.createConnection("users"); // create 'users' collection if not exist
        mongoDBGeneral.createConnection("questions"); // create 'questions' collection if not exist
        mongoDBGeneral.createConnection("results"); // create 'results' collection if not exist

        mongoDatabase = DatabaseGeneral.mongoDatabase;
        Question question = new Question(mongoDatabase, questionHashMap);
        result = new Result(mongoDatabase);
        totalQuestionIndex = Question.totalQuestionIndex;
        Frame loginFrame = new Frame();
        loginFrame.initialize();
                
    }
}
