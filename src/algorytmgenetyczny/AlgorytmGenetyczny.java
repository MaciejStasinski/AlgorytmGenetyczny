package algorytmgenetyczny;

import static java.lang.Math.*;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Willow
 */
public class AlgorytmGenetyczny {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random random = new Random();

        System.out.println("y = a * sin(x + 3) - b * ln(x) - c * x^2 + 16");
        System.out.println("Wprowadź zmienną a:");
        int a = scan.nextInt();
        System.out.println("Wprowadź zmienną b:");
        int b = scan.nextInt();
        System.out.println("Wprowadź zmienną c:");
        int c = scan.nextInt();
//Wprowadzenie podstawowych danych i wzoru funkcji
//---------------------------------------------------
        System.out.println("Podaj ilość osobników: ");
        int iloscOsobnikow = scan.nextInt();
        System.out.println("Podaj Globalne prawdopodobieństwo krzyżowania: ");
        double krzyżowanie = scan.nextDouble();
        System.out.println("Podaj Globalne prawdopodobieństwo mutacji: ");
        double mutacja = scan.nextDouble();
        System.out.println("Podaj liczbę pokoleń: ");
        int liczbaPokoleń = scan.nextInt();
        System.out.println("Przez ile pokoleń śledzić najlepszego osobnika zanim zakończymy eksperyment?");
        int ilePokoleńŚledzić = scan.nextInt();
        int pokolenie = 1;
        int brakZmian = 0;
        int fenotypNajwyższy = 0;
        double najlepszyOsobnik = 0;
        double poprzedniNajlepszy = 0;
        int[][] populacja = new int[iloscOsobnikow][8];
        int[] fenotyp = new int[iloscOsobnikow];
        double[] funkcjaPrzystosowania = new double[iloscOsobnikow];
        System.out.println("Populacja początkowa");
        for (int i = 0; i < populacja.length; i++) {
            System.out.print("osobnik " + (i + 1) + " - ");
            for (int j = 0; j < populacja[i].length; j++) {
                populacja[i][j] = random.nextInt(2);

                System.out.print(populacja[i][j]);
            }
            fenotyp[i] = 128 * populacja[i][0] + 64 * populacja[i][1] + 32 * populacja[i][2] + 16 * populacja[i][3] + 8 * populacja[i][4] + 4 * populacja[i][5] + 2 * populacja[i][6] + 1 * populacja[i][7];
            System.out.print(" fenotyp: " + fenotyp[i]);
            int x = fenotyp[i];
            double y = a * Math.sin(x + 3) - b * Math.log(x) - c * Math.pow(x, 2) + 16;
            funkcjaPrzystosowania[i] = y;
            System.out.println(" funkcja " + funkcjaPrzystosowania[i]);
        }
        System.out.println("");
//Stworzenie populacji początkowej, obliczenie fenotypu

        for (int p = 0; p < liczbaPokoleń; p++) {
            System.out.println("");
            System.out.println("Pokolenie " + pokolenie);
            pokolenie++;
            for (int i = 0; i < iloscOsobnikow; i++) {
                fenotyp[i] = 128 * populacja[i][0] + 64 * populacja[i][1] + 32 * populacja[i][2] + 16 * populacja[i][3] + 8 * populacja[i][4] + 4 * populacja[i][5] + 2 * populacja[i][6] + 1 * populacja[i][7];
                if (fenotyp[i] == 0) {
                    fenotyp[i]++;  // fenotyp nie będzie równy 0 - nie może, bo w funkcji mamy logarytm który wyrzuca wtedy - INFINITY
                }
                int x = fenotyp[i];
                System.out.print("osobnik nr: " + (i + 1) + " fenotyp: " + fenotyp[i]);
                double y = a * sin(x + 3) - b * log(x) - c * pow(x, 2) + 16;
                funkcjaPrzystosowania[i] = y;
                System.out.println(" funkcja " + funkcjaPrzystosowania[i]);
            }
//Wartość przystosowania i fenotyp

            najlepszyOsobnik = Integer.MIN_VALUE;
            int numerNajlepszego = 0;
            double sumaPrzystosowania = 0;
            for (int i = 0; i < iloscOsobnikow; i++) {
                sumaPrzystosowania += funkcjaPrzystosowania[i];
//                System.out.println("Wartość przystosowanie osobnika nr: " + (i + 1) + " - " + funkcjaPrzystosowania[i]);
                if (funkcjaPrzystosowania[i] > najlepszyOsobnik) {
                    najlepszyOsobnik = funkcjaPrzystosowania[i];
                    numerNajlepszego = i;
                    fenotypNajwyższy = fenotyp[i];
                }
            }
            System.out.println("Suma przystosowania :" + sumaPrzystosowania);
            System.out.println("Najlepiej przystosowany osobnik: osobnik: " + (numerNajlepszego + 1) + " - " + najlepszyOsobnik);
            System.out.println("");
//suma przystosowania

            double najsłabszaFunkcja = 0;
            for (int i = 0; i < funkcjaPrzystosowania.length; i++) {
                if (funkcjaPrzystosowania[i] < najsłabszaFunkcja) {
                    najsłabszaFunkcja = funkcjaPrzystosowania[i];
                }
            }
            for (int i = 0; i < funkcjaPrzystosowania.length; i++) {
                if (najsłabszaFunkcja < 0) {
                    funkcjaPrzystosowania[i] += abs(najsłabszaFunkcja) + 1;
                }
            }
            double sumaUjemna = (najsłabszaFunkcja * iloscOsobnikow) - (1 * iloscOsobnikow);

// ujemna wartość przystosowania
            double[] procenty = new double[iloscOsobnikow];
            double[] kołoRuletki = new double[iloscOsobnikow];//przedziały
            double sumaProcentów = 0;
            if (najsłabszaFunkcja < 0) {
                sumaPrzystosowania -= sumaUjemna;
            }
            for (int i = 0; i < iloscOsobnikow; i++) {
                procenty[i] = (funkcjaPrzystosowania[i] / sumaPrzystosowania) * 100;
//                System.out.println("osobnik " + (i + 1) + " - " + procenty[i] + " %");

                kołoRuletki[i] = sumaProcentów + procenty[i];
                sumaProcentów += procenty[i];
//                System.out.println(" Koło ruletki przedziały:" + kołoRuletki[i]);
            }
//Koło ruletki - procenty
            int[] wylosowaneOsobniki = new int[iloscOsobnikow];
            int wynik = 0;

            for (int j = 0; j < iloscOsobnikow; j++) {
                double wylosowanaLiczba = random.nextDouble() * 100;
//                System.out.print("wylosowanaLiczba " + wylosowanaLiczba + " ");
                for (int i = 0; i < iloscOsobnikow; i++) {
                    if (wylosowanaLiczba < kołoRuletki[i]) {
                        wynik = i;
                        break;
                    }
                }
//                System.out.println("wylosowany osobnik= " + (wynik + 1));
                wylosowaneOsobniki[j] = wynik;
            }
//selekcja osobników metodą koła róletki

            boolean[] paryDoRozmnażania = new boolean[iloscOsobnikow / 2];
            for (int i = 0; i < iloscOsobnikow / 2; i++) {
                double czyKrzyżować = random.nextDouble();
//                System.out.println("czy krzyżować: " + czyKrzyżować);
                if (czyKrzyżować < krzyżowanie) {
                    paryDoRozmnażania[i] = true;
                }
            }
//czy zachodzi krzyżowanie

            int licznik = 0;
            int[][] populacjaPotomna = new int[iloscOsobnikow][8];
            for (int i = 0; i < iloscOsobnikow / 2; i++) {
                if (paryDoRozmnażania[i] == true) {
                    int locus = random.nextInt(7) + 1;
//                    System.out.println("locus " + locus);
                    for (int j = 0; j < 8; j++) {
                        if (j < locus) {
                            populacjaPotomna[licznik][j] = populacja[wylosowaneOsobniki[licznik]][j];
                            populacjaPotomna[licznik + 1][j] = populacja[wylosowaneOsobniki[licznik + 1]][j];
                        } else {
                            populacjaPotomna[licznik][j] = populacja[wylosowaneOsobniki[licznik + 1]][j];
                            populacjaPotomna[licznik + 1][j] = populacja[wylosowaneOsobniki[licznik]][j];
                        }
                    }
                } else {
                    for (int j = 0; j < 8; j++) {
                        populacjaPotomna[licznik][j] = populacja[wylosowaneOsobniki[licznik]][j];
                        populacjaPotomna[licznik + 1][j] = populacja[wylosowaneOsobniki[licznik + 1]][j];
                    }
                }
                licznik += 2;
            }
// krzyżowanie

            for (int i = 0; i < iloscOsobnikow; i++) {
                double czyMutować = random.nextDouble();
//                System.out.println("czy mutować: " + czyMutować);
                if (czyMutować < mutacja) {
                    int m = random.nextInt(8);
//                    System.out.println("m= " + m);
                    if (populacjaPotomna[i][m] == 0) {
                        populacjaPotomna[i][m] = 1;
                    } else {
                        populacjaPotomna[i][m] = 0;
                    }
                }
            }
//mutacja

            System.out.println("");
            System.out.println("Populacja potomna");
            for (int i = 0; i < populacja.length; i++) {
                System.out.print("osobnik " + (i + 1) + " - ");
                for (int j = 0; j < populacja[i].length; j++) {
                    System.out.print(populacjaPotomna[i][j]);
                }
                fenotyp[i] = 128 * populacjaPotomna[i][0] + 64 * populacjaPotomna[i][1] + 32 * populacjaPotomna[i][2] + 16 * populacjaPotomna[i][3] + 8 * populacjaPotomna[i][4] + 4 * populacjaPotomna[i][5] + 2 * populacjaPotomna[i][6] + 1 * populacjaPotomna[i][7];
                System.out.println(" fenotyp: " + fenotyp[i]);
            }
            System.out.println("");
//wyświetlenie osobników potomnych

//            System.out.println("nowa populacja podstawowa to: ");
            for (int i = 0; i < iloscOsobnikow; i++) {
                for (int j = 0; j < populacja[i].length; j++) {
                    populacja[i][j] = populacjaPotomna[i][j];
//                    System.out.print(populacja[i][j]);
                }
//                System.out.println("");
            }
//zastąpienie populacji, populacją potomną

            if (najlepszyOsobnik == poprzedniNajlepszy) {
                brakZmian++;
            } else {
                brakZmian = 0;
            }
            if (brakZmian == ilePokoleńŚledzić) {
                break;
            }
            poprzedniNajlepszy = najlepszyOsobnik;
// zakończenie po x pokoleniach bez zmian
        }
        System.out.println("Maksimum funkcji znaleziono dla x= " + fenotypNajwyższy + " i wynosi ono: " + najlepszyOsobnik);
    }
}
