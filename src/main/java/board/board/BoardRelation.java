package board.board;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class BoardRelation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "related_board_id")
    private Board relatedBoard;

    private int relevance;

    public static BoardRelation createBoardRelation(Board board, Board relatedBoard, int relevance) {
        BoardRelation boardRelation = new BoardRelation();
        boardRelation.setBoard(board);
        boardRelation.setRelatedBoard(relatedBoard);
        boardRelation.setRelevance(relevance);

        return boardRelation;
    }
}
