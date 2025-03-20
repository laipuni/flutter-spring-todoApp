package project.app.flutter_spring_todoapp;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@EnableJpaAuditing
@Component
public class AuditingConfiguration {
}
