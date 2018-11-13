package gameMaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import entity.Card;
import entity.Player;

public class GameMaster {

	public void start() {
		BufferedReader in = null;
		LinkedList<Card> deck = new LinkedList<>();
		Card trashed;
		ArrayList<Player> playerList = new ArrayList<>();

		// プレイヤーリスト作成
		for (int i = 0; i < 4; i++) {
			playerList.add(new Player(i, new ArrayList<>()));
		}

		// デッキ作成
		makeDeck(deck);
		// System.out.println(deck);

		// シャッフル
		Collections.shuffle(deck);
		// System.out.println(deck);

		// 一枚横によけるカード
		trashed = deck.poll();
		// System.out.println(trashed);

		// カード配布
		distributeCards(deck, playerList);

		// 効果解決用クラス
		EffectSolver es = new EffectSolver(playerList);

		int currentPlayerNum = 0;

		// ターン開始
		while (deck.iterator().hasNext()) {

			// 脱落人数確認
			int count = 0;
			for (Player player : playerList) {
				if (player.isRetired())
					count++;
			}
			if (count == 3) {
				break;
			}

			currentPlayerNum = currentPlayerNum % 4;

			Player currentPlayer = playerList.get(currentPlayerNum);

			if (currentPlayer.isGuarded()) {
				currentPlayer.setGuarded(false);
			}

			if (currentPlayer.isRetired()) {
				currentPlayerNum++;
				continue;
			}
			System.out.println("Player" + currentPlayerNum + "のターンです。");
			System.out.println("カードを引きます。");

			Card drewCard = deck.poll();

			System.out.println("引いたカード: " + drewCard);

			System.out.println("捨て札");
			playerList.forEach(player -> {
				System.out.println(player.getTrash());
			});

			currentPlayer.getHands().add(drewCard);

			// 大臣チェック
			if (currentPlayer.getHands().stream().mapToInt(Card::getNumber).sum() >= 12) {
				if (currentPlayer.getHands().get(0).getName().equals("大臣")
						|| currentPlayer.getHands().get(1).getName().equals("大臣")) {
					System.out.println("残念、12を超えてしまいました。");
					System.out.println("0: " + currentPlayer.getHands().get(0));
					System.out.println("1: " + currentPlayer.getHands().get(1));
					System.out.println("ですので、死んでくださいね。");
					currentPlayer.setRetired(true);
					continue;
				}
			}

			int handNum;
			while (true) {
				System.out.println("捨てるカードを選択して下さい。");

				System.out.println("0: " + currentPlayer.getHands().get(0));
				System.out.println("1: " + currentPlayer.getHands().get(1));

				String line = "";
				try {
					in = new BufferedReader(new InputStreamReader(System.in));
					line = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				handNum = Integer.parseInt(line);
				if (handNum > 1) {
					System.out.println("0か1を入力してください。");
					continue;
				}
				break;
			}
			Card trashCard = currentPlayer.getHands().get(handNum);
			currentPlayer.getHands().remove(handNum);

			es.solve(deck, trashCard, currentPlayer);
			currentPlayer.getTrash().add(trashCard);

			currentPlayerNum++;
		}

		// デッキ切れ時の終了処理
		if (deck.size() == 0) {
			System.out.println("デッキが無くなったため、各プレイヤーの手札を確認します。");
			System.out.println(playerList);
			Player won = playerList.get(0);
			int max = 0;
			for (Player player : playerList) {
				if (player.getHands().size() == 0)
					continue;

				if (max < player.getHands().get(0).getNumber()) {
					won = player;
					max = player.getHands().get(0).getNumber();
				}
			}

			System.out.println("勝者はplayer" + won.getNumber() + "でした！！");
			System.out.println("おめでとう～");
		}

		// 3名脱落時の終了処理
		if (deck.size() != 0) {
			System.out.println("3名が脱落したので、各プレイヤーの手札を確認します。");
			System.out.println(playerList);

			playerList.forEach(player -> {
				if(!player.isRetired()) {
					Player won = player;
					System.out.println("勝者はplayer" + won.getNumber() + "でした！！");
				}
			});

			System.out.println("おめでとう～");
		}

	}

	private void distributeCards(LinkedList<Card> deck, ArrayList<Player> playerList) {

		playerList.forEach(player -> {
			player.getHands().add(deck.poll());
		});

	}

	private void makeDeck(List<Card> deck) {

		deck.add(new Card(1, "兵士"));
		deck.add(new Card(1, "兵士"));
		deck.add(new Card(1, "兵士"));
		deck.add(new Card(1, "兵士"));
		deck.add(new Card(1, "兵士"));

		deck.add(new Card(2, "道化"));
		deck.add(new Card(2, "道化"));

		deck.add(new Card(3, "騎士"));
		deck.add(new Card(3, "騎士"));

		deck.add(new Card(4, "僧侶"));
		deck.add(new Card(4, "僧侶"));

		deck.add(new Card(5, "魔導士"));
		deck.add(new Card(5, "魔導士"));

		deck.add(new Card(6, "将軍"));

		deck.add(new Card(7, "大臣"));

		deck.add(new Card(8, "姫"));
	}

}
