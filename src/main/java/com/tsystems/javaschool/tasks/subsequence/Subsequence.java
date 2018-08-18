package com.tsystems.javaschool.tasks.subsequence;


import java.util.List;

public class Subsequence {

    public boolean find(List list1, List list2) {
        //проверка корректности введенных данных
        if ((list1 == null) || (list2 == null)) throw new IllegalArgumentException();
        else {
            if (list1.isEmpty()) return true;
            if (list2.isEmpty()) return false;

            int index = 0;
            boolean check = false;
            //проходим по елементам первого списка
            for (int i = 0; i < list1.size(); i++) {
                //если данный элемент не содержится во втором списке, то прекратить проверку с отрицательным результатом
                if (!list2.contains(list1.get(i))) {
                    check = false;
                    break;
                } else {
                    //в переменную index сохраняем индекс предыдущего элемента второго списка
                    int j = index;
                    //перебираем элементы второго списка в поиске элемена из первого списка
                    while (j < list2.size()) {

                        if (list1.get(i) != list2.get(j)) {
                            j++;
                            if (check) check = false;
                        } else {
                            check = true;
                            index = j;
                            break;
                        }
                    }
                    //если второй список кончился раньше, чем перебрали все элементы первого вернуть False
                    if ((j == list2.size()) && (i != list1.size())) {
                        check = false;
                        break;
                    }
                }
            }
            return check;

        }
    }
}
