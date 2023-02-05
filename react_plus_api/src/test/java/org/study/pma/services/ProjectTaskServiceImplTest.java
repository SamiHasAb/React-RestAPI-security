package org.study.pma.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.study.pma.exceptions.ProjectNotFoundException;
import org.study.pma.models.Backlog;
import org.study.pma.models.Project;
import org.study.pma.models.ProjectTask;
import org.study.pma.repositories.BacklogRepository;
import org.study.pma.repositories.ProjectRepository;
import org.study.pma.repositories.ProjectTaskRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectTaskServiceImplTest {

    @Autowired
    private ProjectTaskService projectTaskService;
    @MockBean
    private BacklogRepository backlogRepository;

    @MockBean
    private ProjectTaskRepository projectTaskRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @Mock
    private ProjectService projectService;

    private String projectIdentifier;
    private String username;
    private ProjectTask mockedProjectTask;
    private Project mockedProject;
    private Backlog mockedBacklog;
    @BeforeEach
    void setUp() {
        projectIdentifier = "xyyz";
        username = "user name";

        mockedProjectTask = new ProjectTask();
        mockedProjectTask.setStatus("Active");
        mockedProjectTask.setPriority(0);
        mockedProjectTask.setProjectSequence(projectIdentifier);
        mockedProjectTask.setProjectIdentifier("xyyz");

        mockedBacklog = new Backlog();
        mockedBacklog.setPTSequence(500);
        mockedBacklog.setProjectIdentifier(projectIdentifier);

        mockedProject = new Project();
        mockedProject.setBacklog(mockedBacklog);
        mockedProject.setProjectLeader(username);
    }

    @Test
    void addProjectTask() {

        when(projectTaskRepository.save(mockedProjectTask))
                .thenReturn(mockedProjectTask);
        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);

        ProjectTask actual = projectTaskService
                .addProjectTask(projectIdentifier, mockedProjectTask, username);

        assertEquals(3,actual.getPriority());
    }

    @Test
    void findBacklogById() {

        when(projectTaskRepository.findByProjectIdentifierOrderByPriority("xyyz"))
                        .thenReturn(List.of(mockedProjectTask));
        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);

        Iterable<ProjectTask> actual = projectTaskService
                .findBacklogById("xyyz", username);

        assertEquals(List.of(mockedProjectTask),actual);
    }

    @Test
    void findBacklogById_should_return_ProjectNotFoundException() {

        mockedProjectTask.setProjectSequence(null);

        when(projectTaskRepository.findByProjectIdentifierOrderByPriority("xyyz"))
                .thenReturn(List.of(mockedProjectTask));
        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);

        Iterable<ProjectTask> actual = projectTaskService
                .findBacklogById("xyyz", username);

        assertEquals(List.of(mockedProjectTask),actual);
    }

    @Test
    void findPTByProjectSequence() {

        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);
        when(projectTaskRepository.findByProjectSequence("pt_id"))
                .thenReturn(mockedProjectTask);

        ProjectTask actual = projectTaskService
                .findPTByProjectSequence("xyyz", "pt_id", username);

        assertEquals(mockedProjectTask,actual);
    }
    @Test
    void findPTByProjectSequence_should_return_ProjectNotFoundException() {

        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);
        when(projectTaskRepository.findByProjectSequence(null))
                .thenReturn(mockedProjectTask);

        ProjectNotFoundException actual = assertThrows(ProjectNotFoundException.class,
                () -> projectTaskService
                        .findPTByProjectSequence("xyyz", "pt_id", username));

        assertEquals("Project Task 'pt_id' not found",actual.getMessage());
    }

    @Test
    void findPTByProjectSequence_should_return_ProjectNotFoundException_with_backlog_id() {

        mockedProjectTask.setProjectIdentifier("id");

        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);
        when(projectTaskRepository.findByProjectSequence("pt_id"))
                .thenReturn(mockedProjectTask);

        ProjectNotFoundException actual = assertThrows(ProjectNotFoundException.class,
                ()->projectTaskService
                .findPTByProjectSequence("xyyz", "pt_id", username));

        assertEquals("Project Task 'pt_id' does not exist in project: 'xyyz",
                actual.getMessage());
    }
    @Test
    void updateByProjectSequence() {

        when(projectTaskRepository.save(mockedProjectTask))
                .thenReturn(mockedProjectTask);
        when(projectTaskRepository.findByProjectSequence("pt_id"))
                .thenReturn(mockedProjectTask);
        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);

        ProjectTask actual = projectTaskService
                .updateByProjectSequence(mockedProjectTask,
                "xyyz", "pt_id", username);

        assertEquals(mockedProjectTask,actual);
    }

    @Test
    void deletePTByProjectSequence() {

        when(projectTaskRepository.findByProjectSequence("pt_id"))
                .thenReturn(mockedProjectTask);
        when(projectRepository.findByProjectIdentifier("XYYZ"))
                .thenReturn(mockedProject);

        projectTaskService.deletePTByProjectSequence("xyyz", "pt_id", username);

        verify(projectTaskRepository, times(1)).delete(mockedProjectTask);
    }
}