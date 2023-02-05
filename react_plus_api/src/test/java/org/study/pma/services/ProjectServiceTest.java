package org.study.pma.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.study.pma.exceptions.ProjectIdException;
import org.study.pma.exceptions.ProjectNotFoundException;
import org.study.pma.models.Backlog;
import org.study.pma.models.Project;
import org.study.pma.models.User;
import org.study.pma.repositories.BacklogRepository;
import org.study.pma.repositories.ProjectRepository;
import org.study.pma.repositories.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @MockBean
    private ProjectRepository projectRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BacklogRepository backlogRepository;
    String username;
    Project mockedProject = new Project();
    User mockedUser = new User();
    Backlog mockedBacklog = new Backlog();

    @BeforeEach
    void setUp() {
        username = "user name";

        mockedProject.setId(1L);
        mockedProject.setProjectIdentifier("XYZ");
        mockedProject.setProjectLeader(username);

        mockedUser.setUsername(username);

        mockedBacklog.setId(5L);
    }

    @Test
    void saveOrUpdate_should_not_set_backlog_id_and_return_a_Project() {

        mockedProject.setId(null);

        when(userRepository.findByUsername("user name"))
                .thenReturn(mockedUser);
        when(projectRepository.save(ArgumentMatchers.any(Project.class)))
                .thenReturn(mockedProject);

        Project actual = projectService.saveOrUpdate(mockedProject, username);

        //backlog id should be generated in the repository layer
        assertEquals(null, actual.getBacklog().getId());

    }

    @Test
    void saveOrUpdate_should_return_a_Project() {

        when(projectRepository.findByProjectIdentifier("XYZ"))
                .thenReturn(mockedProject);
        when(userRepository.findByUsername("user name"))
                .thenReturn(mockedUser);
        when(backlogRepository.findByProjectIdentifier("XYZ"))
                .thenReturn(mockedBacklog);
        when(projectRepository.save(ArgumentMatchers.any(Project.class)))
                .thenReturn(mockedProject);

        Project actual = projectService.saveOrUpdate(mockedProject, username);

        assertEquals(5L, actual.getBacklog().getId());

    }

    @Test
    void saveOrUpdate_should_return_a_ProjectNotFoundException() {

        mockedProject.setProjectLeader("wrong projectLeader");


        when(projectRepository.findByProjectIdentifier("XYZ"))
                .thenReturn(mockedProject);

        ProjectNotFoundException actual = assertThrows(ProjectNotFoundException.class,
                () -> projectService.saveOrUpdate(mockedProject, username));

        assertEquals("Project not found in your account",
                actual.getMessage());

    }

    @Test
    void saveOrUpdate_should_return_a_ProjectNotFoundException_with_ID() {

        when(projectRepository.findByProjectIdentifier("XYZ"))
                .thenReturn(null);

        ProjectNotFoundException actual = assertThrows(ProjectNotFoundException.class,
                () -> projectService.saveOrUpdate(mockedProject, username));

        assertEquals("Project with ID: 'XYZ' cannot be updated because it doesn't exist",
                actual.getMessage());

    }

    @Test
    void saveOrUpdate_should_Return_an_ProjectIdException() {

        mockedProject.setId(null);

        when(projectRepository.findByProjectIdentifier("XYZ"))
                .thenReturn(null);

        ProjectIdException actual = assertThrows(ProjectIdException.class,
                () -> projectService.saveOrUpdate(mockedProject, username));

        assertEquals("Project ID 'XYZ' already exists",
                actual.getMessage());

    }

    @Test
    void findProjectByIdentifier_should_return_ProjectIdException() {

        when(projectRepository.findByProjectIdentifier("XXYZ"))
                .thenReturn(null);

        ProjectIdException actual = assertThrows(ProjectIdException.class,
                () -> projectService.findProjectByIdentifier("xxyz", username));

        assertEquals("Can not find project with ID : xxyz.This project does not exist",
                actual.getMessage());
    }

    @Test
    void findProjectByIdentifier_should_return_ProjectNotFoundException() {

        mockedProject.setProjectLeader("wrong project leader");
        when(projectRepository.findByProjectIdentifier("XXYZ"))
                .thenReturn(mockedProject);

        ProjectNotFoundException actual = assertThrows(ProjectNotFoundException.class,
                () -> projectService.findProjectByIdentifier("xxyz", username));

        assertEquals("Project not found in your account",
                actual.getMessage());
    }

    @Test
    void deleteProjectByIdentifier() {

        when(projectRepository.findByProjectIdentifier("XXYZ"))
                .thenReturn(mockedProject);

        projectService.deleteProjectByIdentifier("xxyz", username);

        verify(projectRepository,times(1))
                        .delete(mockedProject);
    }

    @Test
    void deleteProjectByIdentifier_should_return_an_ProjectIdException() {

        when(projectRepository.findByProjectIdentifier("XXYZ"))
                .thenReturn(null);

        ProjectIdException actual = assertThrows(ProjectIdException.class,
                () -> projectService.deleteProjectByIdentifier("xxyz", username));

        assertEquals("Can not find project with ID : xxyz.This project does not exist",
                actual.getMessage());
    }

    @Test
    void getAllProjects_should_return_all_project() {

        when(projectRepository.findAll())
                .thenReturn(List.of(mockedProject));

        Iterable<Project> projects = projectService.getAllProjects();

        assertEquals(List.of(mockedProject),projects);

    }

    @Test
    void findAllProjects_should_return_all_projects_with_the_same_username() {

        when(projectRepository.findAllByProjectLeader(username))
                .thenReturn(List.of(mockedProject));

        Iterable<Project> projects = projectService.findAllProjects(username);

        assertEquals(List.of(mockedProject),projects);
    }
}