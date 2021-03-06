package com.exampleAPI.zooKeeperAPI.controller;

import com.exampleAPI.zooKeeperAPI.model.User;
import com.exampleAPI.zooKeeperAPI.service.UserService;
import com.exampleAPI.zooKeeperAPI.service.ZookeeperService;
import org.apache.zookeeper.KeeperException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.exampleAPI.zooKeeperAPI.support.JsonAndObject.ObjectToJson;
import static com.exampleAPI.zooKeeperAPI.support.testConstant.TEST;
import static com.exampleAPI.zooKeeperAPI.support.testConstant.TEST_PATH_QQ_COM;
import static com.exampleAPI.zooKeeperAPI.support.testConstant.TEST_QQ_COM;
import static com.exampleAPI.zooKeeperAPI.support.testConstant.TEST_QQ_COM_NULL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    public ZookeeperService zookeeperService;

    private UserService userService;

    private String requestJson;

    private User user;

    @Before
    public void setUp() throws IOException, KeeperException, InterruptedException {

        user = new User(TEST_QQ_COM, TEST, 12);

        when(zookeeperService.getData(TEST_PATH_QQ_COM)).thenReturn(user);
        when(zookeeperService.listNodeData()).thenReturn(TEST_QQ_COM_NULL);

        userService = new UserService(zookeeperService);
        requestJson = ObjectToJson(user);
        zookeeperService = mock(ZookeeperService.class);

    }

    @Test
    public void shouldBeReturnStatusIsCreatedWhenRegisterUser() throws Exception {
        mvc.perform(post("/api/user/register")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldBeReturnStatusIsOkWhenUserLogin() throws Exception {
        mvc.perform(post("/api/user/login")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldBeReturnStatusIsOkWhenUserUpdate() throws Exception {
        mvc.perform(put("/api/user/update")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldBeReturnStatusIsOkWhenUserDelete() throws Exception {
        String request = "{\"email\":\"test@qq.com\"}";
        mvc.perform(delete("/api/user/delete?email=test@qq.com"))
                .andExpect(status().isOk());
    }
}
