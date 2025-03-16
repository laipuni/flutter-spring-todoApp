package project.app.flutter_spring_todoapp.todo.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import project.app.flutter_spring_todoapp.IntegrationTestSupport;
import project.app.flutter_spring_todoapp.member.Member;
import project.app.flutter_spring_todoapp.member.Role;
import project.app.flutter_spring_todoapp.member.repository.MemberRepository;
import project.app.flutter_spring_todoapp.todo.domain.Todo;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.domain.TodoStatus;
import project.app.flutter_spring_todoapp.todo.dto.response.TodoItemResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.TodoListResponse;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static project.app.flutter_spring_todoapp.todo.repository.TodoRepositoryCustomImpl.*;

@Transactional
class TodoRepositoryCustomImplTest extends IntegrationTestSupport {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("첫번째 페이지라는 정보 이외에 정보가 빈칸일 경우, 등록 최신순으로 조회한다.")
    @Test
    void findTodosByMemberWithFilters(){
        //given
        Member member = createMember("fcmToken","profile","email@email.com",
                "nickName");
        //다른 유저의 todo도 만들어 사용자의 todo만 조회하는지 테스트
        Member another = createMember("anotherFcmToken","anotherProfile",
                "anotherEmail@email.com","anotherNickName");
        memberRepository.saveAll(List.of(member,another));

        //할일 11개를 미리 저장
        initTodoList(member);
        //다른 유저의 할일도 저장
        todoRepository.save(createTodo(another,0,"another",TodoPriority.LOW));

        int page = 0;
        String search = "";
        String order = "";
        String sort = "";

        //when
        TodoListResponse result = todoRepository.findTodosByMemberWithFilters(member.getId(), page, search, order, sort);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(TODO_PAGE_SIZE);
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getTodoList()).hasSize(TODO_PAGE_SIZE);
    }

    private static Member createMember(String fcmToken, String profile, String email, String nickName) {
        return Member.builder()
                .fcmToken(fcmToken)
                .role(Role.USER)
                .profile(profile)
                .email(email)
                .nickName(nickName)
                .build();
    }

    @DisplayName("마지막 페이지일 경우, 다음 페이지가 없다고 조회된다.")
    @Test
    void findTodosByMemberWithFiltersLastPage(){
        //given
        Member member = createMember("fcmToken","profile","email@email.com",
                "nickName");

        //다른 유저의 todo도 만들어 사용자의 todo만 조회하는지 테스트
        Member another = createMember("anotherFcmToken","anotherProfile",
                "anotherEmail@email.com","anotherNickName");

        memberRepository.saveAll(List.of(member,another));

        //할일 11개를 미리 저장
        initTodoList(member);
        //다른 유저의 할일도 저장
        todoRepository.save(createTodo(another,0,"another",TodoPriority.LOW));

        int page = 1;
        String search = "";
        String order = "";
        String sort = "";

        //when
        TodoListResponse result = todoRepository.findTodosByMemberWithFilters(member.getId(), page, search, order, sort);
        //then
        assertThat(result).isNotNull();
        assertThat(result.isHasNext()).isFalse();
    }

    @DisplayName("검색어와 일치하는 할 일이 하나뿐이면, 해당 할 일만 조회된다.")
    @Test
    void findTodosByMemberWithFiltersAndSingleSearch(){
        //given
        Member member = createMember("fcmToken","profile","email@email.com",
                "nickName");

        //다른 유저의 todo도 만들어 사용자의 todo만 조회하는지 테스트
        Member another = createMember("anotherFcmToken","anotherProfile",
                "anotherEmail@email.com","anotherNickName");

        memberRepository.saveAll(List.of(member,another));

        //할일 11개를 미리 저장
        initTodoList(member);
        //다른 유저의 할일도 저장
        todoRepository.save(createTodo(another,0,"another",TodoPriority.LOW));

        int page = 0;
        String search = "할일8";
        String order = "";
        String sort = "";

        //when
        TodoListResponse result = todoRepository.findTodosByMemberWithFilters(member.getId(), page, search, order, sort);
        //then
        assertThat(result).isNotNull();
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getTodoList()).hasSize(1);
    }

    @DisplayName("등록된 기간이 늦은 순으로 할 일들이 조회된다.")
    @Test
    void findTodosByMemberWithFiltersAndMultipleSearch(){
        //given
        Member member = createMember("fcmToken","profile","email@email.com",
                "nickName");

        //다른 유저의 todo도 만들어 사용자의 todo만 조회하는지 테스트
        Member another = createMember("anotherFcmToken","anotherProfile",
                "anotherEmail@email.com","anotherNickName");

        memberRepository.saveAll(List.of(member,another));

        //할일 11개를 미리 저장
        initTodoList(member);
        //다른 유저의 할일도 저장
        todoRepository.save(createTodo(another,0,"another",TodoPriority.LOW));

        int page = 0;
        String search = "";
        String order = "";
        String sort = "ASC";

        //when
        TodoListResponse result = todoRepository.findTodosByMemberWithFilters(member.getId(), page, search, order, sort);
        //then
        assertThat(result).isNotNull();
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getTodoList()).hasSize(TODO_PAGE_SIZE);
    }

    @DisplayName("우선순위 내림차순으로 검색할 때, 우선순위가 높은 할일 순으로 조회된다.")
    @Test
    void findTodosByMemberWithFiltersAndPriorityDESC(){
        //given
        Member member = createMember("fcmToken","profile","email@email.com",
                "nickName");

        //다른 유저의 todo도 만들어 사용자의 todo만 조회하는지 테스트
        Member another = createMember("anotherFcmToken","anotherProfile",
                "anotherEmail@email.com","anotherNickName");

        memberRepository.saveAll(List.of(member,another));

        //다른 유저의 할일도 저장
        todoRepository.save(createTodo(another,0,"another",TodoPriority.LOW));

        Todo higherTodo = createTodo(member, 1, "HIGH",TodoPriority.HIGH);
        Todo mediumTodo = createTodo(member, 1, "MEDIUM",TodoPriority.MEDIUM);
        Todo lowTodo = createTodo(member, 1, "LOW",TodoPriority.LOW);
        todoRepository.saveAll(List.of(higherTodo,mediumTodo,lowTodo));

        int page = 0;
        String search = "";
        String order = "priority";
        String sort = "";

        //when
        TodoListResponse result = todoRepository.findTodosByMemberWithFilters(member.getId(), page, search, order, sort);
        //then
        assertThat(result).isNotNull();
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getTodoList().get(0).getPriority()).isEqualTo(TodoPriority.HIGH);
        assertThat(result.getTodoList().get(1).getPriority()).isEqualTo(TodoPriority.MEDIUM);
        assertThat(result.getTodoList().get(2).getPriority()).isEqualTo(TodoPriority.LOW);
    }

    @DisplayName("우선순위 오름차순으로 검색할 때, 우선순위가 낮은 할일 순으로 조회된다.")
    @Test
    void findTodosByMemberWithFiltersAndPriorityASC(){
        //given
        Member member = createMember("fcmToken","profile","email@email.com",
                "nickName");

        //다른 유저의 todo도 만들어 사용자의 todo만 조회하는지 테스트
        Member another = createMember("anotherFcmToken","anotherProfile",
                "anotherEmail@email.com","anotherNickName");

        memberRepository.saveAll(List.of(member,another));


        //다른 유저의 할일도 저장
        todoRepository.save(createTodo(another,0,"another",TodoPriority.LOW));

        Todo higherTodo = createTodo(member, 1, "HIGH",TodoPriority.HIGH);
        Todo mediumTodo = createTodo(member, 1, "MEDIUM",TodoPriority.MEDIUM);
        Todo lowTodo = createTodo(member, 1, "LOW",TodoPriority.LOW);

        todoRepository.saveAll(List.of(higherTodo,mediumTodo,lowTodo));

        int page = 0;
        String search = "";
        String order = "priority";
        String sort = "ASC";

        //when
        TodoListResponse result = todoRepository.findTodosByMemberWithFilters(member.getId(), page, search, order, sort);
        //then
        assertThat(result).isNotNull();
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getTodoList().get(0).getPriority()).isEqualTo(TodoPriority.LOW);
        assertThat(result.getTodoList().get(1).getPriority()).isEqualTo(TodoPriority.MEDIUM);
        assertThat(result.getTodoList().get(2).getPriority()).isEqualTo(TodoPriority.HIGH);

    }

    private List<Todo> initTodoList(final Member member) {
        //해당 테스트 클래스에서 자주 쓰일 로직이라 메소드로 선언
        List<Todo> todoList = List.of(
                createTodo(member, 1, "1",TodoPriority.LOW),
                createTodo(member, 2, "2",TodoPriority.LOW),
                createTodo(member, 3, "3",TodoPriority.LOW),
                createTodo(member, 4, "4",TodoPriority.LOW),
                createTodo(member, 5, "5",TodoPriority.LOW),
                createTodo(member, 6, "6",TodoPriority.LOW),
                createTodo(member, 7, "7",TodoPriority.LOW),
                createTodo(member, 8, "8",TodoPriority.LOW),
                createTodo(member, 9, "9",TodoPriority.LOW),
                createTodo(member, 10, "10",TodoPriority.LOW),
                createTodo(member, 11, "11",TodoPriority.LOW)
        );
        todoRepository.saveAll(todoList);
        return todoList;
    }

    private static Todo createTodo(final Member member, final int yearOffset, final String titleOffset,final TodoPriority priority) {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        //테스트 시각 뒤에 마감
        LocalDateTime dutDateTime = LocalDateTime.of(now.getYear() + yearOffset,now.getMonth(),now.getDayOfMonth(),now.getHour(), now.getMinute(),0,0);
        return Todo.builder()
                .title("할일" + titleOffset)
                .member(member)
                .description("할일의 설명")
                .status(TodoStatus.TODO)
                .priority(priority)
                .startDate(now)
                .dueDate(dutDateTime)
                .build();
    }

}