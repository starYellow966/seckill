package web;

import dto.ExecutionResult;
import dto.Exposer;
import dto.SeckillResult;
import entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.SeckillService;

import java.util.Date;
import java.util.List;

@Controller
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    /**
     * 秒杀商品列表页
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String index(Model model){
        List<Seckill> seckills = seckillService.getAllSeckill();
        model.addAttribute("list", seckills);
        return "list";
    }

    /**
     * 秒杀商品详情页
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String getSeckillById(@PathVariable("seckillId") Long seckillId, Model model){
        if (seckillId == null)
            return "redirect:/list";

        try{
            Seckill seckill = seckillService.getSeckillBySeckillId(seckillId);
            if (seckill == null)
                return "forward:/list";
            model.addAttribute("seckill", seckill);
            return "detail";
        } catch (Exception e){
            return "redirect:/list";
        }
    }

    /**
     * 获取商品的秒杀地址
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<Exposer> getExposerUrl(@PathVariable("seckillId") Long seckillId){
        if (seckillId == null)
            return null;
        try{
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            return new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e){
            return new SeckillResult<Exposer>(false, e.getMessage());
        }
    }

    /**
     * 执行秒杀
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<ExecutionResult> execute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "killPhone", required = true) Long userPhone){
        try{
            ExecutionResult executionResult = seckillService.executeSeckill(seckillId, userPhone, md5);
//            ExecutionResult executionResult = seckillService.executeSeckillByProcedure(seckillId, userPhone, md5);  // 调用存储过程执行秒杀（比上一方法更优）
            return new SeckillResult<ExecutionResult>(true, executionResult);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 获取服务器时间
     * @return
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> getSysTime(){
        return new SeckillResult<>(true, new Date().getTime());
    }
}
