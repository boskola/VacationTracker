package positive;

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
	
// It exists already in other test Class - in negative package
//	@Test
//	@Order(1)
//	@WithMockUser(username = "bosko@rbt.bb")
//	public void importEmployees() throws Exception {
//		String fileName = "employee_profiles.csv";
//		MockMultipartFile sampleFile = new MockMultipartFile("file", fileName, "text/csv", isEmployees
//
//		);
//
//		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
//				.multipart("/api/admin/importEmployees");
//
//		mockMvc.perform(multipartRequest.file(sampleFile)).andExpect(status().isOk());
//
//	}

	@Test
	@Order(2)
	@WithMockUser(username = "bosko@rbt.bb")
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
	@WithMockUser(username = "bosko@rbt.bb")
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
	@WithMockUser("user8@rbt.rs")
	public void addUsedVacationDays() throws Exception {
		UsedVacationDTO usedVacation = new UsedVacationDTO();
		usedVacation.setVacationStartDate("2019-02-02");
		usedVacation.setVacationEndDate("2019-02-02");

		String url = "/api/employee/addUsedVacationDays";
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(usedVacation);

		mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8).content(requestJson)).andExpect(status().isOk());

	}

	@Test
	@Order(5)
	@WithMockUser(username = "bosko@rbt.bb")
	public void searchTotalAdmin() throws Exception {
		String year = "2019";
		String url = "/api/admin/adminSearch";
		MvcResult result = mockMvc
				.perform(get(url).param("daysOption", "total").param("year", year).param("userEmail", "user2@rbt.rs"))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}

	@Test
	@Order(6)
	@WithMockUser(username = "bosko@rbt.bb")
	public void searchUsedAdmin() throws Exception {
		String year = "2019";
		String url = "/api/admin/adminSearch";
		MvcResult result = mockMvc
				.perform(get(url).param("daysOption", "used").param("year", year).param("userEmail", "user2@rbt.rs"))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}

	@Test
	@Order(7)
	@WithMockUser(username = "bosko@rbt.bb")
	public void searchAvailableAdmin() throws Exception {
		String year = "2019";
		String url = "/api/admin/adminSearch";
		MvcResult result = mockMvc.perform(
				get(url).param("daysOption", "available").param("year", year).param("userEmail", "user1@rbt.rs"))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println("Available "+content);
	}

	@Test
	@Order(8)
	@WithMockUser(username = "bosko@rbt.bb")
	public void searchUsedVacationDaysAdmin() throws Exception {
		String dateFromParam = "2010-11-11";
		String dateToParam = "2023-11-11";
		String url = "/api/admin/adminSearchUsedVacationDays";
		MvcResult result = mockMvc.perform(get(url).param("dateFromParam", dateFromParam)
				.param("dateToParam", dateToParam).param("userEmailParam", "user2@rbt.rs")).andExpect(status().isOk())
				.andReturn();
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

	@Test
	@Order(10)
	@WithMockUser("user1@rbt.rs")
	public void searchUsed() throws Exception {
		String year = "2019";
		String url = "/api/employee/search";
		MvcResult result = mockMvc.perform(get(url).param("daysOption", "used").param("year", year))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}

	@Test
	@Order(11)
	@WithMockUser("user1@rbt.rs")
	public void searchAvailable() throws Exception {
		String year = "2019";
		String url = "/api/employee/search";
		MvcResult result = mockMvc.perform(get(url).param("daysOption", "available").param("year", year))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}

	@Test
	@Order(12)
	@WithMockUser("user1@rbt.rs")
	public void searchUsedVacationDays() throws Exception {
		String dateFromParam = "2010-11-11";
		String dateToParam = "2023-11-11";
		String url = "/api/employee/searchUsedVacationDays";
		MvcResult result = mockMvc
				.perform(get(url).param("dateFromParam", dateFromParam).param("dateToParam", dateToParam))
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
	}
}
