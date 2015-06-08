package com.monetizationservice.services;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.ezpaymentprocessing.model.PurchaseRequest;
import com.ezpaymentprocessing.model.QualifyPromotionFact;

public class PromotionService 
{
	private static KieServices ks;
	private static KieContainer kc;
	
	public static void start()
	{
		System.out.print("Starting JBoss BRMS Rules Runtime...");
        // KieServices is the factory for all KIE services 
        ks = KieServices.Factory.get();
        
        // From the kie services, a container is created from the classpath
        kc = ks.getKieClasspathContainer();
        System.out.println("Started");
	}
	
	/**
	 * Quick rule to qualify a purchase.
	 * @param purchaseRequest
	 * @return String representing the promotion, or null if not qualified
	 */
	public static QualifyPromotionFact qualifiy(PurchaseRequest purchaseRequest)
	{
		

        // From the container, a session is created based on  
        // its definition and configuration in the META-INF/kmodule.xml file 
        KieSession ksession = kc.newKieSession("PromotionsKS");
        // Once the session is created, the application can interact with it

        // Setup listeners for debugging
        ksession.addEventListener( new DebugAgendaEventListener() );
        ksession.addEventListener( new DebugRuleRuntimeEventListener() );

        // Create the fact objects
        QualifyPromotionFact fact = new QualifyPromotionFact();
        fact.setPurchaseAmount(purchaseRequest.getAmount());
        fact.setMerchantId(purchaseRequest.getMerchantId());
        
        // Insert the fact(s) into the session
        ksession.insert( fact );

        // and fire the rules
        ksession.fireAllRules();
        
        System.out.println("Rules result: " + fact.getMessage());

        // Dispose the session
        ksession.dispose();
        return fact;

        
    }
	
}
