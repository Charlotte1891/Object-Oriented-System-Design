package edu.stanford.cs108.tetris;


public class TetrisBrainLogic extends TetrisLogic {
    private DefaultBrain brain;
    protected boolean brainMode = false;
    private Brain.Move bestMove;
    private int currCount;


    // new constructor
    public TetrisBrainLogic(TetrisUIInterface uiInterface) {
        super(uiInterface);
        brain = new DefaultBrain();
        bestMove = new Brain.Move();
        currCount = 0;

    }

    // allows clients to tell it whether or not to use the brain.
    public void setBrainMode(boolean brain) {
        brainMode = brain;
    }



    @Override
    protected void tick(int verb) {
        if(brainMode == true && verb == DOWN){
            if (currentPiece != null) {
                board.undo();	// remove the piece from its old position
            }

            if(currCount != super.count){
                currCount = super.count;
                bestMove = brain.bestMove(board, currentPiece, HEIGHT, bestMove);
            }

            if(!bestMove.piece.equals(currentPiece)){
                super.tick(ROTATE);
            }

            if(bestMove.x < currentX){
                super.tick(LEFT);
            }

            if(bestMove.x > currentX) {
                super.tick(RIGHT);
            }
        }
        super.tick(verb);
    }
}
