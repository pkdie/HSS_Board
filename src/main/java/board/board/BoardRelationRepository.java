package board.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRelationRepository {

    private final EntityManager em;

    public void save(BoardRelation boardRelation) {em.persist(boardRelation);}

    public List<BoardRelation> findByBoardId(Long boardId) {
        return em.createQuery("select b from BoardRelation b join fetch b.relatedBoard " +
                        "where b.board.id = :boardId order by b.relevance desc", BoardRelation.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }
}
