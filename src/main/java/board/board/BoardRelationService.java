package board.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardRelationService {

    private final BoardRelationRepository boardRelationRepository;

    @Transactional(readOnly = false)
    public void save(BoardRelation boardRelation) {boardRelationRepository.save(boardRelation);}

    public List<BoardRelation> findByBoardId(Long boardId) {
        return boardRelationRepository.findByBoardId(boardId);
    }


}
