package board.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = false)
    public void save(Board board) {
        boardRepository.save(board);
    }

    public Board find(Long id) {
        return boardRepository.findById(id).get();
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }
}
