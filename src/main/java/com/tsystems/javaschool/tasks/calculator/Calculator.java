package com.tsystems.javaschool.tasks.calculator;


import java.util.Stack;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */


    public String evaluate(String statement) {
        try {
            //проверяем строку на корректность
            if (!validationStatements(statement)) return null;
            // приводим к постфиксному виду
            String res = rpn(statement);
            //осуществляем вычисления постфиксного представления функции
            Double d = calculateRPN(res);
            //приводим вид выводимого значения к ожидаемому виду
            if (d == Math.floor(d)) {
                return Integer.toString(d.intValue());
            } else {
                return d.toString();
            }
            //обрабатываем исключение деления на ноль
        } catch (ArithmeticException e) {
            return null;
        }
    }

    //метод проверки строки на корректно введенные данные
    private boolean validationStatements(String s) {
        if (s == null || s.isEmpty()) return false;
        char[] ch = s.toCharArray();
        boolean checkOp = true, checkDot = true;
        int contSkob = 0;
        for (int i = 0; i < s.length(); i++) {
            if (!isOper(ch[i]) && !isDigit(ch[i]) && !isDelim(ch[i]) && ch[i] != ')' && ch[i] != '(') return false;
            if (ch[i] == ')') contSkob++;
            if (ch[i] == '(') contSkob--;
            if (isOper(ch[i]) && !checkOp && (ch[i] != '(' && ch[i] != ')')) return false;
            if (isOper(ch[i]) && (ch[i] != '(' && ch[i] != ')')) checkOp = false;
            if (!isOper(ch[i]) || (ch[i] == '(' || ch[i] == ')')) checkOp = true;
            if (ch[i] == '.' && !checkDot) return false;
            if (ch[i] == '.') checkDot = false;
            if (ch[i] != '.') checkDot = true;
        }
        return contSkob == 0;
    }

    // метод подсчета выражения записанного в форме обратной польской записи
    private double calculateRPN(String input) {
        char[] ch = input.toCharArray();
        Stack<Double> digStack = new Stack();
        for (int i = 0; i < ch.length; i++) {
            if (isDelim(ch[i])) {
                continue;
            }
            if (isDigit(ch[i])) {
                String temp = "";
                while (!isOper(ch[i]) && !isDelim(ch[i])) {
                    temp += ch[i];
                    i++;
                    if (i == ch.length) break;
                }
                i--;
                digStack.push(Double.parseDouble(temp));
            }
            if (isOper(ch[i])) {
                digStack.push(calculate(digStack.pop(), digStack.pop(), ch[i]));
            }
        }


        return digStack.pop();

    }

    //метод осуществляющий математические операции над 2 переданными переменными (передаются переменные и символ выполняемой операции)
    private double calculate(double b, double a, char ch) {
        switch (ch) {
            case '/': {
                if (b == 0) throw new ArithmeticException();
                return a / b;
            }
            case '*':
                return a * b;
            case '+':
                return a + b;
            case '-':
                return a - b;
            default:
                return -1;
        }
    }

    //приведение инфинксного выражения к постфиксному
    private String rpn(String input) {
        char[] ch = input.toCharArray();

        Stack<Character> operStack = new Stack();
        String result = "";
        for (int i = 0; i < input.length(); i++) {

            if (isDelim(ch[i]))
                continue;

            if (isDigit(ch[i])) {
                while (!isDelim(ch[i]) && !isOper(ch[i])) {

                    result += ch[i];
                    i++;
                    if (i == input.length()) break;
                }
                result += " ";
                i--;
            } else if (ch[i] == '(') {
                operStack.push(ch[i]);

            } else if (ch[i] == ')') {
                char temp = operStack.pop();
                while (temp != '(') {
                    if (operStack.empty()) return null;
                    result += temp;
                    temp = operStack.pop();
                }
            } else if (isOper(ch[i])) {
                if (operStack.empty()) operStack.push(ch[i]);
                else {

                    while (!operStack.empty() && (getPriority(ch[i]) <= getPriority(operStack.peek()))) {
                        result += operStack.pop();

                    }
                    operStack.push(ch[i]);

                }
            }


        }
        while (!operStack.empty()) {
            result += operStack.pop();
        }
        return result;
    }

    //проверка на то, что символ является опреатором или скобкой
    private boolean isOper(char ch) {
        return "+-/*()".indexOf(ch) != -1;
    }

    //проверка на то, что символ является разделителем
    private boolean isDelim(char ch) {
        return " ".indexOf(ch) != -1;
    }

    //проверка на то,что символ является числом
    private boolean isDigit(char ch) {
        return "1234567890.".indexOf(ch) != -1;
    }

    //метод возвращающий приоритет операции
    private int getPriority(char ch) {
        switch (ch) {
            case '/':
                return 4;
            case '(':
                return 0;
            case '+':
                return 2;
            case '-':
                return 3;
            case '*':
                return 4;
            case ')':
                return 1;
            default:
                throw new IllegalArgumentException();
        }

    }

}
