package controllers;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import static org.junit.Assert.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class JavaSessionTest {
    @Test
    public void testgoGet(){
        JavaSession session = mock(JavaSession.class,Mockito.CALLS_REAL_METHODS);
        HttpServletRequest  request = mock(HttpServletRequest.class);
        HttpServletResponse  response = mock(HttpServletResponse.class);
        session.doGet(request,response);
    }
}