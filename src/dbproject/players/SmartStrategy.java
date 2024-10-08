package dbproject.players;

import dbproject.game.Game;
import java.util.HashSet;
import java.util.Set;

/**
 * smart strategy used by AI Player for Dots and Boxes game.
 */
public class SmartStrategy implements Strategy {
    /**
     * Returns next legal move, given the current state of the game.
     *
     * @param game to determine the next legal move from.
     * @return next legal move, given the current state of the game.
     */
    @Override
    public int determineMove(Game game) {
        int winningMove = findWinningMove(game);

        if (winningMove != -1) {
            assert game.isValidMove(winningMove);
            return winningMove;
        }

        int[] moves = game.getValidMoves();
        if (moves.length == 0) {
            return -1;
        }

        Set<Integer> allowedMoves = new HashSet<>();
        for (int j : moves) {
            allowedMoves.add(j);
        }

        for (int move : moves) {
            Game copiedGame2 = game.deepCopy();
            copiedGame2.doMove(move);
            int opponentWinning = findWinningMove(copiedGame2);
            if (opponentWinning != -1) {
                allowedMoves.remove(move);
            }
        }

        if (allowedMoves.isEmpty()) {
            int[] movesArray = game.getValidMoves();
            int random = (int) (Math.random() * movesArray.length);
            assert game.isValidMove(movesArray[random]);
            return movesArray[random];
        }

        int[] result = new int[allowedMoves.size()];
        int i = 0;
        for (int move : allowedMoves) {
            result[i++] = move;
        }
        int random = (int) (Math.random() * result.length);
        assert game.isValidMove(result[random]);
        return result[random];
    }

    /**
     * return a move that gives the current player 2 points, or if this does not exist, a move
     * that gives the current player 1 point, or -1 if there does not exist a move that gives
     * the current player a point.
     *
     * @param game to get a winning move from
     * @return a winning move, or -1 if there does not exist a winning move
     */
    public int findWinningMove(Game game) {
        for (int move : game.getValidMoves()) {
            Game copiedGame = game.deepCopy();
            String computerPlayer = copiedGame.getTurn();
            int score = copiedGame.getPlayerScore(computerPlayer);
            copiedGame.doMove(move);
            int updatedScore = copiedGame.getPlayerScore(computerPlayer);
            if (updatedScore == score + 2) {
                return move;
            } else if (updatedScore == score + 1) {
                return move;
            }
        }
        return -1;
    }
}
