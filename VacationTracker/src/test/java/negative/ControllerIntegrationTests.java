package negative;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import project.ProjectApplication;
import project.controller.AdminController;
import project.dto.UsedVacationDTO;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = ProjectApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTests {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private InputStream isEmployees, isUsedVacation, isVacation1, isVacation2, isVacation3;
	
	@Autowired
	private WebApplicationContext applicationContext;
	
	private MockMvc mockMvc;

	@Spy
	@InjectMocks
	private AdminController controller = new AdminController();

	@BeforeAll
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();

		isEmployees = controller.getClass().getClassLoader().getResourceAsStream("employee_profiles.csv");
		isUsedVacation = controller.getClass().getClassLoader().getResourceAsStream("used_vacation_dates.csv");
		isVacation1 = controller.getClass().getClassLoader().getResourceAsStream("vacations_2019.csv");
		isVacation2 = controller.getClass().getClassLoader().getResourceAsStream("vacations_2020.csv");
		isVacation3 = controller.getClass().getClassLoader().getResourceAsStream("vacations_2021.csv");

	}

	@Test
	@Order(1)
	public void importEmployees() throws Exception {
		String fileName = "employee_profiles.csv";
		MockMultipartFile sampleFile = new MockMultipartFile("file", fileName, "text/csv", isEmployees

		);

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/admin/importEmployees");

		mockMvc.perform(multipartRequest.file(sampleFile)).andExpect(status().isOk());

	}

	@Test
	@Order(2)
	public void importUsedVacation() throws Exception {
		String fileName = "used_vacation_dates.csv";
		MockMultipartFile sampleFile = new MockMultipartFile("file", fileName, "text/csv", isUsedVacation

		);

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/admin/importUsedVacation");

		mockMvc.perform(multipartRequest.file(sampleFile)).andExpect(status().isOk());
	}

	@Test
	@Order(3)
	public void importVacation() throws Exception {
		String fileName1 = "vacations_2019.csv", fileName2 = "vacations_2020.csv", fileName3 = "vacations_2021.csv";
		MockMultipartFile sampleFile1 = new MockMultipartFile("file", fileName1, "text/csv", isVacation1);
		MockMultipartFile sampleFile2 = new MockMultipartFile("file", fileName2, "text/csv", isVacation2);
		MockMultipartFile sampleFile3 = new MockMultipartFile("file", fileName3, "text/csv", isVacation3);

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/admin/importYearTotalVacationDays");

		mockMvc.perform(multipartRequest.file(sampleFile1)).andExpect(status().isOk());
		mockMvc.perform(multipartRequest.file(sampleFile2)).andExpect(status().isOk());
		mockMvc.perform(multipartRequest.file(sampleFile3)).andExpect(status().isOk());
	}

	@Test
	@Order(4)
	@WithMockUser("user1@rbt.rs")
	public void addUsedVacationDays() throws Exception {
		UsedVacationDTO usedVacation = new UsedVacationDTO();
		usedVacation.setVacationStartDate("2019-07-02"); // start date is after begin
		usedVacation.setVacationEndDate("2019-02-02");

		String url = "/api/employee/addUsedVacationDays";
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(usedVacation);

		mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(5)
	public void searchTotalAdmin() throws Exception {
		String year = "2019";
		String url = "/api/employee/adminSearch";
		MvcResult result = mockMvc
				.perform(get(url).param("daysOption", "total").param("year", year).param("userEmail", "user0@rbt.rs")) // user
																														// doesn't
																														// exist
				.andExpect(status().isBadRequest()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}

	@Test
	@Order(6)
	public void searchUsedAdmin() throws Exception {
		String year = "2019";
		String url = "/api/employee/adminSearch";
		MvcResult result = mockMvc.perform(get(url).param("daysOption", "use") // bad input
				.param("year", year).param("userEmail", "user2@rbt.rs")).andExpect(status().isBadRequest()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}

	@Test
	@Order(7)
	public void searchAvailableAdmin() throws Exception {
		String year = "2030"; // bad year, without data
		String url = "/api/employee/adminSearch";
		mockMvc.perform(
				get(url).param("daysOption", "available").param("year", year).param("userEmail", "user2@rbt.rs"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(
						"The user has used more vacation days than intended: 0! User has no more available vacation days!"))
				.andReturn();
	}

	@Test
	@Order(8)
	public void searchUsedVacationDaysAdmin() throws Exception {
		String dateFromParam = "2010-11-11";
		String dateToParam = "2023-11-aaa"; // not date
		String url = "/api/employee/adminSearchUsedVacationDays";
		MvcResult result = mockMvc.perform(get(url).param("dateFromParam", dateFromParam)
				.param("dateToParam", dateToParam).param("userEmailParam", "user2@rbt.rs"))
				.andExpect(status().isBadRequest()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}

	@Test
	@Order(9)
	@WithMockUser("user1@rbt.rs")
	public void searchTotal() throws Exception {
		String year = "2019";
		String url = "/api/employee/search";
		MvcResult result = mockMvc.perform(get(url).param("daysOption", "total").param("year", year))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}
}
