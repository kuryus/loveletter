package gameMaster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import entity.Card;
import entity.Player;

public class EffectSolver {

	ArrayList<Player> playerList;

	public EffectSolver(ArrayList<Player> playerList) {
		super();
		this.playerList = playerList;
	}

	void solve(LinkedList<Card> deck, Card card, Player currentPlayer) {
		Scanner sc = new Scanner(System.in);
		String line;
		Player opponentPlayer;
		Card opponentHand;
		Card currentHand;
		int objectCard;

		switch (card.getNumber()) {
		case 1:
			opponentPlayer = selectPlayer();
			opponentHand = opponentPlayer.getHands().get(0);

			// 僧侶効果解決
			if (opponentPlayer.isGuarded()) {
				System.out.println("効果は無効化された！");
				break;
			}

			objectCard = selectCard();

			if (opponentHand.getNumber() == objectCard) {
				System.out.println("敵" + opponentPlayer + "を倒した");
				opponentPlayer.getTrash().add(opponentHand);
				opponentPlayer.getHands().clear();
				opponentPlayer.setRetired(true);
			} else {
				System.out.println("どうやら違うようだ。");
			}

			break;
		case 2:
			opponentPlayer = selectPlayer();

			// 僧侶効果解決
			if (opponentPlayer.isGuarded()) {
				System.out.println("効果は無効化された！");
				break;
			}

			System.out.println("敵" + opponentPlayer + "のカードは" + opponentPlayer.getHands().get(0) + "だったぞ！");
			break;

		case 3:
			opponentPlayer = selectPlayer();

			// 僧侶効果解決
			if (opponentPlayer.isGuarded()) {
				System.out.println("効果は無効化された！");
				break;
			}

			opponentHand = opponentPlayer.getHands().get(0);
			currentHand = currentPlayer.getHands().get(0);

			if (currentHand.getNumber() > opponentHand.getNumber()) {
				System.out.println("敵" + opponentPlayer.getNumber() + "を倒した");
				System.out.println("自分のカード: " + currentHand);
				System.out.println("相手のカード: " + opponentHand);
				opponentPlayer.getTrash().add(opponentHand);
				opponentPlayer.getHands().clear();
				opponentPlayer.setRetired(true);
			} else if (currentHand.getNumber() < opponentHand.getNumber()) {
				System.out.println("自分が倒されてしまった");
				System.out.println("自分のカード: " + currentHand);
				System.out.println("相手のカード: " + opponentHand);
				currentPlayer.getTrash().add(currentHand);
				currentPlayer.getHands().clear();
				currentPlayer.setRetired(true);
			} else {
				System.out.println("引き分けだ！");
				System.out.println("自分のカード: " + currentHand);
				System.out.println("相手のカード: " + opponentHand);
			}
			break;

		case 4:
			System.out.println("player" + currentPlayer.getNumber() + "は守りに入った。");
			currentPlayer.setGuarded(true);
			break;

		case 5:
			opponentPlayer = selectPlayer();

			// 僧侶効果解決
			if (opponentPlayer.isGuarded()) {
				System.out.println("効果は無効化された！");
				break;
			}

			Card trashing = opponentPlayer.getHands().get(0);
			opponentPlayer.getHands().remove(0);
			opponentPlayer.getTrash().add(trashing);

			opponentPlayer.getHands().add(deck.poll());

			System.out.println(trashing + "を捨てた。");

			break;

		case 6:
			opponentPlayer = selectPlayer();

			// 僧侶効果解決
			if (opponentPlayer.isGuarded()) {
				System.out.println("効果は無効化された！");
				break;
			}

			opponentHand = opponentPlayer.getHands().get(0);
			currentHand = currentPlayer.getHands().get(0);

			opponentPlayer.getHands().remove(0);
			currentPlayer.getHands().remove(0);

			opponentPlayer.getHands().add(currentHand);
			currentPlayer.getHands().add(opponentHand);

			System.out.println("カード交換だ！");

			break;

		case 7:
			System.out.println("大臣が捨てられたか…");

			break;
		case 8:
			System.out.println("姫を捨てるなんてとんでもない！");
			System.out.println("player" + currentPlayer.getNumber() + "はしんでしまった！！！");

			currentPlayer.setRetired(true);

			break;

		default:
			break;
		}

	}

	private Player selectPlayer() {
		Scanner sc = new Scanner(System.in);
		Player selectedPlayer;
		String line;

		while (true) {
			while (true) {
				System.out.println("相手を選択してください: 0, 1, 2, 3");
				line = sc.nextLine();

				if (!line.equals("0") && !line.equals("1") && !line.equals("2") && !line.equals("3")) {
					System.out.println("0 ~ 3の数字を入力してください。");
					continue;
				}

				int playerNum = Integer.parseInt(line);

				selectedPlayer = playerList.get(playerNum);

				// 2018/11/21 kyuuu948 死んだプレイヤーを選択できてしまう処理の修正
				if(selectedPlayer.isRetired()) {
					System.out.println("選択したプレイヤーは死んでいます。生きているプレイヤーを選択してください。");
					continue;
				}

				break;
			}

			if (selectedPlayer.isGuarded()) {
				int tmp;

				while (true) {
					System.out.println("効果が無効化されます。よろしいですか？");
					System.out.println("0: いいえ、選びなおします。");
					System.out.println("1: はい、大丈夫です。");

					line = sc.nextLine();
					tmp = Integer.parseInt(line);

					if (tmp > 1) {
						System.out.println("0か1を入力してください。");
						continue;
					}
					break;
				}
				if (tmp == 0) {
					continue;
				} else if (tmp == 1) {
					break;
				}
			}
			break;
		}
		return selectedPlayer;
	}

// 2018/11/20 kyuuu948 兵士カード選択処理追加
	private int selectCard(){

		Scanner sc = new Scanner(System.in);
		String line;

			while (true) {
				System.out.println("数字を選択してください: 2[道化], 3[騎士], 4[僧侶], 5[魔術師], 6[将軍], 7[大臣], 8[姫]");
				line = sc.nextLine();


				if (!line.equals("2") && !line.equals("3") && !line.equals("4") && !line.equals("5") && !line.equals("6") && !line.equals("7") && !line.equals("8")) {
					System.out.println("2 ~ 8の数字を入力してください。");
					continue;
				}

				int cardNum = Integer.parseInt(line);

				return cardNum;

			}



	}
}