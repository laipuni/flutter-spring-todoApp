package project.app.flutter_spring_todoapp.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;
import project.app.flutter_spring_todoapp.todo.domain.TodoPriority;
import project.app.flutter_spring_todoapp.todo.dto.response.TodoItemResponse;
import project.app.flutter_spring_todoapp.todo.dto.response.TodoListResponse;

import java.util.LinkedList;
import java.util.List;

import static project.app.flutter_spring_todoapp.todo.domain.QTodo.todo;

public class TodoRepositoryCustomImpl implements TodoRepositoryCustom{

    public static final int TODO_PAGE_SIZE = 10;

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryCustomImpl(final EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public TodoListResponse findTodosByMemberWithFilters(final Long memberId, final int page, final String search, final String order, final String sort) {
        Pageable pageable = PageRequest.of(page,TODO_PAGE_SIZE);
        boolean hasNext = false;
        List<TodoItemResponse> content = queryFactory.select(Projections.constructor(TodoItemResponse.class,
                        todo.id, todo.title, todo.description, todo.startDate,
                        todo.dueDate, todo.status, todo.priority
                ))
                .from(todo)
                .where(filter(memberId,search))
                .orderBy(getOrderBy(order,sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        if(content.size() > TODO_PAGE_SIZE){
            //한 슬라이드의 개수는 총 10개인데, 10개보다 많은 경우 다음 슬라이스가 있다고 판단
            hasNext = true;
            //마지막 원소는 제거
            content.remove(TODO_PAGE_SIZE);
        }
        return TodoListResponse.of(content,hasNext);
    }

    private BooleanBuilder filter(final Long memberId, final String search){
        BooleanBuilder builder = new BooleanBuilder();
        if(StringUtils.hasText(search)){
            builder.and(todo.title.contains(search));
        }
        builder.and(todo.member.id.eq(memberId));
        return builder;
    }

    private OrderSpecifier[] getOrderBy(final String order,final String sort){
        if(!StringUtils.hasText(order)){
            //order 혹은 sort가 빈 공백일 경우
            //최근 등록한 순으로 정렬
            return new OrderSpecifier[]{new OrderSpecifier<>(Order.DESC, todo.createdTime)};
        }

        List<OrderSpecifier> orders = new LinkedList<>();
        Order direction = sort.equalsIgnoreCase("ASC") ? Order.ASC : Order.DESC;
        if (order.equalsIgnoreCase("priority")) {
            //우선순위가 높은 순, 우선순위가 같다면 최근 등록된 순
            orders.add(new OrderSpecifier<>(direction, getPriorityOrder()));
            //등록한 순서대로
            orders.add(new OrderSpecifier<>(Order.DESC, todo.createdTime));
        } else{
            //등록된 날짜에 오름차순 혹은 내림차순으로 정렬
            orders.add(new OrderSpecifier<>(direction, todo.createdTime));
        }
        return orders.toArray(OrderSpecifier[]::new);
    }

    private NumberExpression<Integer> getPriorityOrder() {
        return new CaseBuilder()
                .when(todo.priority.eq(TodoPriority.LOW)).then(1)
                .when(todo.priority.eq(TodoPriority.MEDIUM)).then(2)
                .when(todo.priority.eq(TodoPriority.HIGH)).then(3)
                .otherwise(99);
    }

}
