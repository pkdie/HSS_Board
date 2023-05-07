package board.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final WordAnalysisService wordAnalysisService;
    private final BoardRelationService boardRelationService;

    @GetMapping("/")
    public String home(Model model) {
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);
        return "home";
    }

    @GetMapping("/boardForm")
    public String boardForm(Model model) {
        model.addAttribute("boardForm", new BoardForm());
        return "createBoardForm";
    }

    @PostMapping("/boardForm")
    public String createBoardForm(BoardForm boardForm) {
        Board board = Board.createBoard(boardForm.getTitle(), boardForm.getContent());
        boardService.save(board);
        List<Board> boards = boardService.findAll();
        wordAnalysisService.frequencyCheck(board, boards);
        return "redirect:/";
    }

    @GetMapping("/{boardId}")
    public String boardDetail(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.find(boardId);
        List<BoardRelation> relatedBoards = boardRelationService.findByBoardId(boardId);
        model.addAttribute("relatedBoards", relatedBoards);
        model.addAttribute("board", board);
        return "boardDetail";
    }
}
