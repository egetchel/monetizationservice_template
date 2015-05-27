package com.monetizationservice.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ezpaymentprocessing.utils.ConfigManager;
import com.ezpaymentprocessing.utils.RestClient;

/**
 * Servlet implementation class
 * 
 *  This is to get around the same origin policy that prevents us from posting cross-domain.  Essentially, it forwards the call to the REST Purchase endpoint
 *  to be processed.
 *  
 */
@WebServlet("/PurchaseServlet/*")
public class PurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurchaseServlet() {
        super();
       System.out.println("PurchaseServlet started");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get invoked");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String merchantId = request.getParameter("merchantId");
		String amount = request.getParameter("amount");
		String mobileNumber = request.getParameter("mobileNumber");
		System.out.println("POST: Amount: "+ request.getParameter("amount"));
		RestClient client = new RestClient();
		client.sendGet(ConfigManager.getPaymentProcessingURL(), "merchantId="+merchantId+"&amount="+amount+"&mobileNumber="+mobileNumber);
		
	}

}
