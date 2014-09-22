package pl.warsjawa.legacycode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pl.warsjawa.legacycode.infra.DB;

@Ignore
public class SbsOrderDaoTest {

    private SbsOrderDao dao;

    @Before
    public void setUp() {
        dao = SbsOrderDao.getInstance();
        DB.start();
//        DB.runScript("createDB.sql");
    }
    
    @Test
    public void findOrderById() throws Exception {

        Order order = new Order();
        order.setId("234");
        order.setStatus("NEW");
        order.setTotalPrice(123);
        order.setCustomerData(new CustomerData("jdoe@mail.com", "John Doe"));
        dao.save(order);

        order = dao.findOrderById("123");
        assertThat(order).isNull();
        
        order = dao.findOrderById("234");
        assertThat(order).isNotNull();
    }
}
