package com.kata.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class BankTests {

    // Connaître le solde de son compte
    // Faire un dépôt
    // Faire un retrait => pas de solde negatif, si on a pas assez sur le compte, alors le retrait ne fait rien
    // Faire la liste des transactions sous la forme
    //  Date           transaction   balance (après)
    //  14/01/2012 14:30:45.450    //-500.00     //2500.00
    //  13/01/2012 07:14:23.200    //2000.00     //3000.00
    //  10/01/2012 23:52:31.753    //1000.00     //1000.00

    @Test
    public void when_balance_is_100_then_show_100() {
        // Given
        Bank bank = new Bank();
        bank.balance = 100;
        // When
        float balance = bank.getBalance();
        // Then
        Assertions.assertEquals(100, balance);
    }

    @Test
    public void when_deposit_1000_and_balance_is_100_then_balance_is_1100() {
        // Given
        Bank bank = new Bank();
        bank.balance = 100;
        // When
        bank.deposit(1000, "10/01/2012 23:52:31.753");
        // Then
        float balance = bank.getBalance();
        Assertions.assertEquals(1100, balance);
    }

    @Test
    public void when_withdraw_500_and_balance_is_2500_then_balance_is_2000() {
        // Given
        Bank bank = new Bank();
        bank.balance = 2500;
        // When
        bank.withdraw(500, "10/01/2012 23:52:31.753");
        // Then
        float balance = bank.getBalance();
        Assertions.assertEquals(2000, balance);
    }
    @Test
    public void when_withdraw_500_and_balance_is_1500_then_balance_is_1000() {
        // Given
        Bank bank = new Bank();
        bank.balance = 1500;
        // When
        bank.withdraw(500, "10/01/2012 23:52:31.753");
        // Then
        float balance = bank.getBalance();
        Assertions.assertEquals(1000, balance);
    }

    @Test
    public void when_withdraw_1000_and_balance_is_500_then_balance_is_500() {
        // Given
        Bank bank = new Bank();
        bank.balance = 500;
        // When
        bank.withdraw(1000, "10/01/2012 23:52:31.753");
        // Then
        float balance = bank.getBalance();
        Assertions.assertEquals(500, balance);
    }

    @Test
    public void when_deposit_1000_with_balance_0_then_i_have_one_transaction() {
        // Given
        Bank bank = new Bank();
        bank.balance = 0;
        // When
        bank.deposit(1000, "10/01/2012 23:52:31.753");
        // Then
        ArrayList<String> transactions = bank.listTransactions();
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("10/01/2012 23:52:31.753 1000,00 1000,00");
        Assertions.assertIterableEquals(expected, transactions);
    }

    @Test
    public void when_deposit_2000_with_balance_0_then_i_have_one_transaction() {
        // Given
        Bank bank = new Bank();
        bank.balance = 0;
        // When
        bank.deposit(2000, "10/01/2012 23:52:31.753");
        // Then
        ArrayList<String> transactions = bank.listTransactions();
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("10/01/2012 23:52:31.753 2000,00 2000,00");
        Assertions.assertIterableEquals(expected, transactions);
    }

    @Test
    public void when_deposit_2000_with_balance_1000_then_i_have_one_transaction() {
        // Given
        Bank bank = new Bank();
        bank.balance = 1000;
        // When
        bank.deposit(2000, "10/01/2012 23:52:31.753");
        // Then
        ArrayList<String> transactions = bank.listTransactions();
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("10/01/2012 23:52:31.753 2000,00 3000,00");
        Assertions.assertIterableEquals(expected, transactions);
    }

    @Test
    public void when_2_deposit_2000_with_balance_1000_then_i_have_two_transactions() {
        // Given
        Bank bank = new Bank();
        bank.balance = 1000;
        // When
        bank.deposit(2000, "10/01/2012 23:52:31.753");
        bank.deposit(2000, "10/01/2012 23:53:31.753");
        // Then
        ArrayList<String> transactions = bank.listTransactions();
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("10/01/2012 23:52:31.753 2000,00 3000,00");
        expected.add("10/01/2012 23:53:31.753 2000,00 5000,00");
        Assertions.assertIterableEquals(expected, transactions);
    }

    @Test
    public void when_deposit_2000_withdraw_1000_with_balance_1000_then_i_have_two_transactions() {
        // Given
        Bank bank = new Bank();
        bank.balance = 1000;
        // When
        bank.deposit(2000, "10/01/2012 23:52:31.753");
        bank.withdraw(1000, "10/01/2012 23:53:31.753");
        // Then
        ArrayList<String> transactions = bank.listTransactions();
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("10/01/2012 23:52:31.753 2000,00 3000,00");
        expected.add("10/01/2012 23:53:31.753 -1000,00 2000,00");
        Assertions.assertIterableEquals(expected, transactions);
    }

    // { “ibanFrom”: “xxxxx”, “ibanTo”: “xxxxx”, “amount”: 200.00 }

    // j'ai 1000, je transfert -500, il me reste 1000
    // j'ai 1000, je transfert 500 sans émetteur, il me reste 1000


    @Test
    void i_transfer_400() {
        // Given
        Bank bank = new Bank();
        TransfertService transfertService = Mockito.spy(new TransfertService());
        bank.transfertService=transfertService;

        // When
        bank.transfert("toto", "titi", 400);

        // Then
        Mockito.verify(transfertService, Mockito.times(1)).transfert("toto", "titi", 400);
    }

    @Test
    void i_transfer_200_from_senderIban_to_receiverIban() {
        // Given
        Bank bank = new Bank();
        TransfertService transfertService = Mockito.spy(new TransfertService());
        bank.transfertService=transfertService;

        // When
        bank.transfert("senderIban", "receiverIban", 200);

        // Then
        Mockito.verify(transfertService, Mockito.times(1)).transfert("senderIban", "receiverIban", 200);
    }

    // j'ai 1000, je transfert 800 sans émetteur, il me reste 200
    @Test
    void when_i_have_1000_and_transfer_800_then_i_have_200() {
        // Given

        TransfertService transfertService = Mockito.mock(TransfertService.class);
        Mockito.when(transfertService.transfert("toto","titi",800)).thenReturn("202");
        Bank bank = new Bank();
        bank.balance=1000;
        bank.transfertService=transfertService;

        // When
        bank.transfert("toto","titi",800);

        // Then
        float balance = bank.getBalance();
        Assertions.assertEquals(200, balance);
    }

    // j'ai 1000, je transfert 800 sans émetteur, il me reste 200
    @Test
    void when_i_have_1000_and_transfer_1000_then_i_have_0() {
        // Given

        TransfertService transfertService = Mockito.mock(TransfertService.class);
        Mockito.when(transfertService.transfert("toto","titi",1000)).thenReturn("202");
        Bank bank = new Bank();
        bank.balance=1000;
        bank.transfertService=transfertService;

        // When
        bank.transfert("toto","titi",1000);

        // Then
        float balance = bank.getBalance();
        Assertions.assertEquals(0, balance);
    }

    // j'ai 1000, je transfert 500 sans destinataire, il me reste 1000
    @Test
    void when_i_have_1000_and_transfer_500_without_receiver_then_i_have_1000() {
        // Given

        TransfertService transfertService = Mockito.mock(TransfertService.class);
        Mockito.when(transfertService.transfert("toto","",500)).thenReturn("400");
        Bank bank = new Bank();
        bank.balance=1000;
        bank.transfertService=transfertService;

        // When
        bank.transfert("toto","",500);

        // Then
        float balance = bank.getBalance();
        Assertions.assertEquals(1000, balance);
    }

}
