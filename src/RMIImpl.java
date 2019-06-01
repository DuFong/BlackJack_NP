import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class RMIImpl extends UnicastRemoteObject implements BlackJack {
	private static final long serialVersionUID = 1L;
	public RMIImpl() throws RemoteException{
		super();
	}
	
	//Initializing Cards, Suites, Money
	public static final int INITIAL_MONEY = 100;
	public static final String[] suites = {"H","S","C","D"}; 
	public static final String[] cards = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
	
	public static int drawCard(List<Card> newDeck, int playerTotal, List<Card> playersCards) throws RemoteException
	{
		int total = 0;
		Card playerCard1 = newDeck.remove(0);
		playerCard1.printCard();
		total += playerCard1.giveValue(playerTotal);
		playersCards.add(playerCard1);
		return total;
	}
	
	public static List<Card> buildDeck(String[] suites, String[] name) throws RemoteException
	{
		List<Card> deck = new ArrayList<Card>();
		for (int i = 0; i < suites.length; i++){
			for (int j = 0; j < name.length; j++){
				Card k = new Card(name[j], suites[i]);
				deck.add(k);
			}
		}
		return deck;
	}
	
	public static List<Card> shuffleDeck(List<Card> deck) throws RemoteException
	{
		List<Card> shuffledDeck = new ArrayList<Card>();
		int r = 0;
		while (deck.size() > 0){
			Random card = new Random();
			r = card.nextInt(deck.size());
			Card temp = deck.remove(r);
			shuffledDeck.add(temp);
		}
		return shuffledDeck;
	}
	
	public static int winCheck(int total, int dealer, int to_play) throws RemoteException
	{
		int gains_losses = 0;
		if (total == 21) {
			System.out.println("You have: " + total);
			System.out.println("You got BlackJack!  You win!");
			gains_losses = 2 * to_play;
		} else if (total > 21) {
			System.out.println("You have: " + total);
			System.out.println("You bust");
			gains_losses = -1 * to_play;
		} else if (total == dealer) {
			System.out.println("You have: " + total);
			System.out.println("Dealer has: " + dealer);
			System.out.println("Push.");
			gains_losses = 0;
		} else if (dealer > 21) {
			System.out.println("Dealer has: " + dealer);
			System.out.println("Dealer busts.  You win!");
			gains_losses = 2 * to_play;
		} else if (total < dealer) {
			System.out.println("You have: " + total);
			System.out.println("Dealer has: " + dealer);
			System.out.println("Dealer wins.");
			gains_losses = -1 * to_play;
		} else {
			System.out.println("You have: " + total);
			System.out.println("Dealer has: " + dealer);
			System.out.println("You beat the dealer!");
			System.out.println("You win!");
			gains_losses = 2 * to_play;
		}
		return gains_losses;
	}
	
	public static int betAmount(int money, Scanner console) throws RemoteException
	{
		System.out.println("How much would you like to bet?");
		int bet = Math.abs(console.nextInt());
		while(bet > money || bet < 10){
			if (bet < 10) {
				System.out.println("There is a minimum bet of $10");
			} else {
		   	System.out.println("You don't have that much money.");
			}
			System.out.println("How much would you like to bet?");
			bet = console.nextInt();
		}
		return bet;
	}
	
	public static boolean Hit(int total, Scanner console) throws RemoteException
	{
		boolean ans = false;
		System.out.println();
		System.out.println("You have: " + total); 
		System.out.println("Do you want to hit?");
      String answer = console.next();
        if (answer.indexOf("y") == 0 || answer.indexOf("Y") == 0) {
            ans = true;
        } else if (answer.indexOf("n") == 0 || answer.indexOf("N") == 0) {
            ans = false;
        }	else {
            System.out.println();
            ans = false;
        }
		return ans;
	}	
	
	public static boolean playAgain(Scanner console, int money) throws RemoteException
	{
		boolean ans;
		System.out.println("You have: $" + money);
		if (money == 0) {
			System.out.println("You're out of money.  House wins.");
			ans = false;
			return ans;
		} 
		System.out.println("Do you want to play again?");
		String answer = console.next();
      if (answer.indexOf("y") == 0 || answer.indexOf("Y") == 0) {
      	ans = true;
			return ans;
      } else if (answer.indexOf("n") == 0 || answer.indexOf("N") == 0) {
            ans = false;
				if (money > 100) {
					System.out.println("Congratulations! You won: $" + (money - 100));
				} else {
					System.out.println("You lost: $" + (100 - money));
				}
				return ans;
      } else {
      	System.out.println();
         ans = false;
      }
		return ans;
	}
	
	public static void intro(int money) throws RemoteException
	{
		System.out.println("Welcome to BlackJack!");
		System.out.println();
		System.out.println("You have: $" + money);
   }
	
	public static class Card
	{
		   private int value;
		   private String name;
		   private String suite;
		   private boolean Ace;
		   
		 
		   public Card(String name, String suite){
			   this.name = name;
			   this.suite = suite;
			   this.value = determineCardValue(name);
		   }
		   public void printCard(){
			   System.out.println(this.name + " of " + this.suite);
		   	}
		   public int giveValue(int playerTotal){
	   			return this.value;
	   		}
		   public boolean isAce(){
	   			return Ace;
	   		}
		   private int determineCardValue(String name) throws NumberFormatException{
				int value = 0;
				try{
					value = Integer.parseInt(name.substring(0,1));
					return value;
				} catch (NumberFormatException e){
					if (name.charAt(0) == 'K' || name.charAt(0) == 'J' || name.charAt(0) == 'Q' || name.charAt(0) == '0'){
						value = 10;
					} else if (name.charAt(0) =='A'){
						value = 11;
						this.Ace = true;
					} else {
						value = Integer.parseInt(name.substring(0, 1)); 
					}
					return value;
				}
		   }
	 }
	
	public void play() throws RemoteException
	{
			Scanner console = new Scanner(System.in);
			List<Card> newDeck = new ArrayList<Card>();
			newDeck = buildDeck(suites, cards);
			int money = INITIAL_MONEY;
			intro(money);
			boolean play = true;
			
			while (money > 0 && play == true)
			{
				//Running card score
				int playerTotal = 0;
				int dealerTotal = 0;
				
				//For future development(Split, Double Down, ect..)
				List<Card> playersCards = new ArrayList<Card>();
				List<Card> dealersCards = new ArrayList<Card>();
				
				//deck setup
				newDeck = shuffleDeck(newDeck);
				int roundBet = betAmount(money, console);
				
				//Initial Deal
				System.out.println("First card: ");
				playerTotal += drawCard(newDeck, playerTotal, playersCards);
				
				System.out.println("Second card: ");
				playerTotal += drawCard(newDeck, playerTotal, playersCards);
				System.out.println();
				
				System.out.println("Dealer showing: ");
				dealerTotal += drawCard(newDeck, dealerTotal, dealersCards);
				System.out.println("Dealer has: " + dealerTotal);
				
				//Player play portion
				boolean another_card = true;
				while(playerTotal < 21 && another_card)
				{
					another_card = Hit(playerTotal, console);
					if(playerTotal > 21 || playerTotal == 21 || !another_card)
						break;
					else
						playerTotal += drawCard(newDeck, dealerTotal, dealersCards);
					
					for(int i = 0; i < playersCards.size(); i++)
						if(playersCards.get(i).isAce() && playerTotal >21)
							playerTotal -= 10;
				}
				
				//Dealer play portion
				while (dealerTotal < 17 && playerTotal < 21)
				{
					System.out.println("Dealer showing: " + dealerTotal);
					Card dealerCard = newDeck.remove(0);
					
					System.out.println("Dealer gets: ");
					dealerCard.printCard();
					dealerTotal += dealerCard.giveValue(dealerTotal);
					dealersCards.add(dealerCard);
					
					//doesn't work
					for(int i = 0; i < dealersCards.size(); i++)
						if (dealersCards.get(i).isAce() && dealerTotal > 21)
							playerTotal -= 10;
				}

				//Decide who wins and whether to play another round
				System.out.println();
				money += winCheck(playerTotal, dealerTotal, roundBet);
				play = playAgain(console, money);
			}
	}

}
