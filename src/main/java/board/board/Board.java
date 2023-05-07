package board.board;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private LocalDate createdDate;

    public static Board createBoard(String title, String content) {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setCreatedDate(LocalDate.now());

        return board;
    }
}
