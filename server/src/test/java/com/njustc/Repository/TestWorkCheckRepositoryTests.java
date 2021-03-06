package com.njustc.Repository;


import com.njustc.FrameworkApplication;
import com.njustc.domain.TestWorkCheck;
import com.njustc.repository.TestWorkCheckRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 这个类用来测试TestFunction类对应repository的增删查功能
 * <br>
 *<table border="1" summary="">
 *     <tr>
 *     <th><b>测试内容</b></th>
 *     <th><b>对应操作</b></th>
 *     <th><b>测试结果</b></th>
 *     </tr>
 *     <tr>
 *         <td>新建测试工作检查表</td>
 *         <td>new + save</td>
 *         <td>FindById成功</td>
 *      </tr>
 *      <tr>
 *          <td>删除该表</td>
 *         <td>delete对应Id</td>
 *         <td>FindById为空</td>
 *      </tr>
 * </table>
 * @author FW
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(FrameworkApplication.class)
public class TestWorkCheckRepositoryTests {
    @Autowired
    private TestWorkCheckRepository testWorkCheckRepository;

    @Test
  //  @Transactional
    public void testTesReport(){
        TestWorkCheck testWorkCheck = new TestWorkCheck();
        testWorkCheck.setId("workcheck");
        testWorkCheckRepository.save(testWorkCheck);
        TestWorkCheck testWorkCheckfind =testWorkCheckRepository.findById("workcheck");
        Assert.assertNotNull("TestWorkCheck为空",testWorkCheckfind);

        testWorkCheckRepository.save(testWorkCheck);
        testWorkCheckRepository.delete("workcheck");
        testWorkCheckfind =null;
        testWorkCheckfind = testWorkCheckRepository.findById("workcheck");
        Assert.assertNull("testReportCheck不为空",testWorkCheckfind);
    }
}
