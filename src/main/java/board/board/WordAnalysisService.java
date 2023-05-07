package board.board;


import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordAnalysisService {

    Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
    private final BoardService boardService;
    private final BoardRelationRepository boardRelationRepository;

    @Transactional(readOnly = false)
    public void frequencyCheck(Board newBoard, List<Board> oldBoards) {
        List<List<String>> oldBoardsNouns = new ArrayList<>(); // 기존 게시글 내용의 단어들을 담을 List 선언
        List<Long> oldBoardsId = new ArrayList<>(); // 기존 게시글의 Id를 담을 List 선언
        List<String> newBoardNounsList = komoran.analyze(newBoard.getContent()).getNouns(); // 새로운 게시글 내용에서 단어를 List로 가져옴
        Set<String> newBoardNounsSet = new HashSet<>(newBoardNounsList); // newBoardNounsList에서 중복을 제거함

        for (Board oldBoard : oldBoards) {
            List<String> oldBoardNouns = komoran.analyze(oldBoard.getContent()).getNouns();
            oldBoardsNouns.add(oldBoardNouns); // 기존 게시글 내용의 단어들을 oldBoardsNouns에 담음
            Long id = oldBoard.getId();
            oldBoardsId.add(id); // 기존 게시글의 Id를 담음
        }

        for (String newBoardNoun : newBoardNounsSet) {
            int cnt = 0;
            for (List<String> oldBoardNouns : oldBoardsNouns) {
                if (oldBoardNouns.contains(newBoardNoun)) { // 만약 기존 게시글 단어가 새로운 게시글의 단어를 포함하고 있다면
                    cnt += 1;
                }
            }
            double percent = (double) cnt / (double) oldBoardsNouns.size() * 100; // 기존 게시글 단어의 빈도 계산
            if (percent > 40) {
                while (newBoardNounsList.remove(newBoardNoun)) {}; // 새로운 게시글 단어에서 40초과 빈도로 나타나는 단어는 List에서 삭제
            }
        } // 이 반복문이 끝나면 Set와 List에서는 40% 이하 빈도의 단어만 남는다

        for (List<String> oldBoardNouns : oldBoardsNouns) {
            int relevance = 0; // relevance는 새로운 게시글과 기존 게시글의 연관성 정도이다
            List<String> matchList = newBoardNounsSet.stream().filter(n -> oldBoardNouns.stream()
                    .anyMatch(Predicate.isEqual(n))).collect(Collectors.toList()); // newBoardNounsSet와 oldBoardNouns의 중복을 찾아 List로 반환
            if (matchList.size() >= 2) {
                // 40% 이하 빈도의 단어가 두개 이상 동시에 나타남
                for (String matchNoun : matchList) {
                    int frequency = Collections.frequency(oldBoardNouns, matchNoun); // 기존 게시글 단어에 40% 이하 빈도의 단어가 나오는 개수
                    relevance += frequency;
                }
                int i = oldBoardsNouns.indexOf(oldBoardNouns); // 연관관계에 해당하는 기존 게시글의 인덱스 번호를 가져옴
                Long boardId = oldBoardsId.get(i); // 연관관계에 해당하는 기존 게시글의 id값을 가져옴
                Board oldBoard = boardService.find(boardId);
                BoardRelation boardRelation = BoardRelation.createBoardRelation(newBoard, oldBoard, relevance);
                BoardRelation reverseBoardRelation = BoardRelation.createBoardRelation(oldBoard, newBoard, relevance);
                if (!newBoard.getId().equals(oldBoard.getId())) {
                    boardRelationRepository.save(boardRelation);
                    boardRelationRepository.save(reverseBoardRelation);
                }
            }
        }
    }
}
