package com.ssg.telcopulse.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.ssg.telcopulse.config.JpaAuditingConfig;
import com.ssg.telcopulse.model.AddonPackage;
import com.ssg.telcopulse.model.Bill;
import com.ssg.telcopulse.model.BillStatus;
import com.ssg.telcopulse.model.Subscriber;
import com.ssg.telcopulse.model.SubscriberProfile;
import com.ssg.telcopulse.model.SubscriberStatus;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class SubscriberRepositoryTest {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired 
    private SubscriberProfileRepository profileRepository;


    @Test 
    void shouldExistsByMsisdn_success()
    {
        Subscriber subscriber = new Subscriber("Rob Alice","+1122334455","Mock Plan",SubscriberStatus.PENDING);
        
        subscriberRepository.save(subscriber);
        assertTrue(subscriberRepository.existsByMsisdn("+1122334455"));

    }

    @Test 
    void shouldExistsByMsisdn_noData()
    {
        assertFalse(subscriberRepository.existsByMsisdn("+1111222233"));
    }


    @Test
    void shouldFindAllWithAddOn_success()
    {

        Subscriber subscriber1 = new Subscriber("Test Data1","+1122334455","Mock Plan1",SubscriberStatus.PENDING);
        Subscriber subscriber2 = new Subscriber("Test Data2","+1234567890","Mock Plan2",SubscriberStatus.PENDING);

        Set<Subscriber> subscriberSet = new HashSet<>();
        AddonPackage addon = new AddonPackage("Unlimited SMS", new BigDecimal("120.00"), subscriberSet);

        subscriber1.addAddOnPackage(addon);
        subscriber2.addAddOnPackage(addon);

        subscriberRepository.save(subscriber1);
        subscriberRepository.save(subscriber2);

        List<Subscriber> subscriberList = subscriberRepository.findAllWithAddOn();

        assertEquals(2, subscriberList.size());
        assertTrue(subscriberList.stream().allMatch(sub -> sub.getAddon().contains(addon)));        

    }


    @Test
    void shouldFindAllWithAddOn_noAddonData()
    {
        Subscriber subscriber1 = new Subscriber("Test Data1","+1122334455","Mock Plan1",SubscriberStatus.PENDING);
        Subscriber subscriber2 = new Subscriber("Test Data2","+1234567890","Mock Plan2",SubscriberStatus.PENDING);

        subscriberRepository.save(subscriber1);
        subscriberRepository.save(subscriber2);

        List<Subscriber> subscriberList = subscriberRepository.findAllWithAddOn();

        assertEquals(2, subscriberList.size());
        assertTrue(subscriberList.stream().allMatch(sub -> sub.getAddon().isEmpty()));        

    }

  @Test
    void shouldFindAllWithAddOn_noSubscriberData()
    {
        List<Subscriber> subscriberList = subscriberRepository.findAllWithAddOn();

        assertTrue(subscriberList.isEmpty());       
    }


    @Test
    void shouldFindAllWithBillsAndProfiles_success()
    {
        //@Query("select distinct sub from Subscriber sub LEFT JOIN FETCH sub.bills LEFT JOIN FETCH sub.profile")
         //List<Subscriber> findAllWithBills();

        Subscriber subscriber1 = new Subscriber("Test Data1","+1122334455","Mock Plan1",SubscriberStatus.PENDING);
        Subscriber subscriber2 = new Subscriber("Test Data2","+1234567890","Mock Plan2",SubscriberStatus.PENDING);

        SubscriberProfile profile1= new SubscriberProfile("testemail@email.com","Kyc1234","7 Via Roma, Milan, Italy");

        Bill bill1 = new Bill("B1001",new BigDecimal("650.00"),LocalDate.now(),BillStatus.PENDING);
        Bill bill2 = new Bill("B1002",new BigDecimal("75.00"),LocalDate.now(),BillStatus.OVERDUE);

        Bill bill3 = new Bill("B2001",new BigDecimal("50.00"),LocalDate.now(),BillStatus.PENDING);
        Bill bill4 = new Bill("B2002",new BigDecimal("490.00"),LocalDate.now(),BillStatus.GENERATED);

        subscriber1.addBill(bill1);
        subscriber1.addBill(bill2);

        subscriber2.addBill(bill3);
        subscriber2.addBill(bill4);

        subscriberRepository.save(subscriber1);
        subscriberRepository.save(subscriber2);

        subscriber1.setProfile(profile1);  
        profile1.setSubscriber(subscriber1);
        profileRepository.save(profile1);


        List<Subscriber> subscribersWithBills = subscriberRepository.findAllWithBills();

        assertFalse(subscribersWithBills.isEmpty());

        assertEquals(2, subscribersWithBills.size());

        Subscriber result1= subscribersWithBills.stream().filter(sub->sub.getMsisdn().equals("+1122334455")).findFirst().orElseThrow();
        Subscriber result2= subscribersWithBills.stream().filter(sub->sub.getMsisdn().equals("+1234567890")).findFirst().orElseThrow();

        // Verify subscriber 1's bills
        assertEquals(2, result1.getBills().size());
        assertTrue(result1.getBills().stream().anyMatch(bill -> bill.getBillNumber().equals("B1001")));
        assertTrue(result1.getBills().stream().anyMatch(bill -> bill.getBillNumber().equals("B1002")));

        // Verify subscriber 2's bills
        assertEquals(2, result2.getBills().size());
        assertTrue(result2.getBills().stream().anyMatch(bill -> bill.getBillNumber().equals("B2001")));
        assertTrue(result2.getBills().stream().anyMatch(bill -> bill.getBillNumber().equals("B2002")));

        // Verify profile
        assertNotNull(result1.getProfile());
        assertEquals("testemail@email.com", result1.getProfile().getEmail());
        assertNull(result2.getProfile());

    }


     @Test
    void shouldFindAllWithBills_noBillData()
    {
        Subscriber subscriber1 = new Subscriber("Test Data1","+1122334455","Mock Plan1",SubscriberStatus.PENDING);
        Subscriber subscriber2 = new Subscriber("Test Data2","+1234567890","Mock Plan2",SubscriberStatus.PENDING);

        SubscriberProfile profile1= new SubscriberProfile("testemail@email.com","Kyc1234","7 Via Roma, Milan, Italy");

        subscriberRepository.save(subscriber1);
        subscriberRepository.save(subscriber2);

        subscriber1.setProfile(profile1);  
        profile1.setSubscriber(subscriber1);
        profileRepository.save(profile1);

        List<Subscriber> subscribersWithBills = subscriberRepository.findAllWithBills();

        assertFalse(subscribersWithBills.isEmpty());

        assertEquals(2, subscribersWithBills.size());

        Subscriber result1= subscribersWithBills.stream().filter(sub->sub.getMsisdn().equals("+1122334455")).findFirst().orElseThrow();
        Subscriber result2= subscribersWithBills.stream().filter(sub->sub.getMsisdn().equals("+1234567890")).findFirst().orElseThrow();

        // Verify no bill for subscriber 1
        assertTrue(result1.getBills().isEmpty());

        // Verify no bill for subscriber 2
        assertTrue(result2.getBills().isEmpty());

        // Verify profile
        assertNotNull(result1.getProfile());
        assertEquals("testemail@email.com", result1.getProfile().getEmail());
        assertNull(result2.getProfile());
        
    }


    @Test
    void shouldFindAllWithBills_noSubscriberData()
    {
        List<Subscriber> subscriberList = subscriberRepository.findAllWithBills();

        assertTrue(subscriberList.isEmpty());       
    }
 
}
