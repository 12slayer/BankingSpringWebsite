package com.bankingapplication.controller;


import com.bankingapplication.model.AmountwithAccountNumber;
import com.bankingapplication.model.ApplicationMessage;
import com.bankingapplication.model.TransferFund;
import com.bankingapplication.model.User;
import com.bankingapplication.repo.UserRepository;
import com.bankingapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/")
public class HomeController {


    @Autowired
    private UserService userService;

    @GetMapping(value="")
    public ModelAndView goHome() {
        ModelAndView modelAndView = new ModelAndView();
        List<User> userList = userService.getAllUser();
        modelAndView.addObject("userList", userList);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping(value="create")
    public ModelAndView showUserForm(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("create");
        modelAndView.addObject("user",new User());
        return modelAndView;

    }

   @PostMapping(value = "create")
    public String addUser(@ModelAttribute User user){
       Random random = new Random();
       long randomNumber = random.nextLong();

       user.setAccountNumber(randomNumber);
       user.setBalance(0);
       userService.addUser(user);
       return "redirect:/";

   }

    @GetMapping(value="/edit/{userId}")
    public ModelAndView showEditUser(@PathVariable int userId){
        ModelAndView modelAndView=new ModelAndView();

        User fetchedUser=userService.findUser(userId);
        modelAndView.addObject("user",fetchedUser);
        modelAndView.setViewName("edit");
        System.out.println(userId);
        return modelAndView;

    }

    @GetMapping(value ="/delete-user/{userId}" )
    public String deleteuser(@PathVariable int userId){

        userService.deleteUser(userId);


        System.out.println(userId);

        return "redirect:/";

    }

    @GetMapping(value = "deposite")
    public String showDepositeForm(){
    /*    ModelAndView modelAndView=new ModelAndView();

       User user= userService.findUserByAccountNumber(accountNumber);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("deposite");
        return modelAndView;*/
        return "deposite";

    }

    @PostMapping(value = "deposite")
    @ResponseBody
    public ApplicationMessage depositePost(@RequestBody AmountwithAccountNumber amountwithAccountNumber){
        ApplicationMessage applicationMessage=new ApplicationMessage();
        User fetchedUser=userService.findUserByAccountNumber(amountwithAccountNumber.getAccountNumber());
        if(fetchedUser==null){
            applicationMessage.setSuccess(false);
            applicationMessage.setMessage("user does not exist");
        }else{
            fetchedUser.setBalance(fetchedUser.getBalance()+amountwithAccountNumber.getBalance());
            fetchedUser = userService.addUser(fetchedUser);

            applicationMessage.setSuccess(true);
            applicationMessage.setBody(fetchedUser);
        }
        return applicationMessage;

    }

    @GetMapping(value = "search-user")
    @ResponseBody
    public ApplicationMessage searchUserByAccountNumber(@RequestParam("account-number") Long accountNumber){
        ApplicationMessage applicationMessage=new ApplicationMessage();
        User user=userService.findUserByAccountNumber(accountNumber);
        if(user==null){
            applicationMessage.setSuccess(false);
            applicationMessage.setMessage("user does not exist");
        }else{
            applicationMessage.setSuccess(true);
            applicationMessage.setBody(user);
        }

        System.out.println(accountNumber);
        return applicationMessage;
    }

    @GetMapping(value = "withdraw")
    public String showWithdrawForm(){
    /*    ModelAndView modelAndView=new ModelAndView();

       User user= userService.findUserByAccountNumber(accountNumber);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("deposite");
        return modelAndView;*/
        return "withdraw";

    }

    @PostMapping(value = "withdraw")
    @ResponseBody
    public ApplicationMessage withdrawPost(@RequestBody AmountwithAccountNumber amountwithAccountNumber){
        ApplicationMessage applicationMessage=new ApplicationMessage();
        User fetchedUser=userService.findUserByAccountNumber(amountwithAccountNumber.getAccountNumber());
        if(fetchedUser==null){
            applicationMessage.setSuccess(false);
            applicationMessage.setMessage("user does not exist");
        }else{
            if(fetchedUser.getBalance()==0 || fetchedUser.getBalance()<amountwithAccountNumber.getBalance()){
                System.out.println("your balance is low");
            }else {
                fetchedUser.setBalance(fetchedUser.getBalance() - amountwithAccountNumber.getBalance());
                fetchedUser = userService.addUser(fetchedUser);

                applicationMessage.setSuccess(true);
                applicationMessage.setBody(fetchedUser);
            }
        }
        return applicationMessage;

    }
    @GetMapping(value = "transfer")
    public String showTranferForm(){
    /*    ModelAndView modelAndView=new ModelAndView();

       User user= userService.findUserByAccountNumber(accountNumber);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("deposite");
        return modelAndView;*/
        return "transfer";

    }

    @PostMapping(value="/transfer")
    @ResponseBody
    public ApplicationMessage processTransferAmount(@RequestBody TransferFund transferFundModel){
        ApplicationMessage applicationMessage = new ApplicationMessage();

        // 1. Find User related to sourceAccountNumber
        // 2. Find User related to destinationAccountNumber


        User sourceAccount = userService.findUserByAccountNumber(transferFundModel.getSourceAccountNumber());
        User desinationAccount = userService.findUserByAccountNumber(transferFundModel.getDestinationAccountNumber());

        if(sourceAccount == null || desinationAccount == null) {
            applicationMessage.setSuccess(false);
            applicationMessage.setMessage("This user doesn't exist in our system");
        } else if( transferFundModel.getTransferAmount() > sourceAccount.getBalance()) {
            applicationMessage.setSuccess(false);
            applicationMessage.setMessage("Not Sufficient Balance!");
        } else {
            // 3. Deduct amount in source
            // 4. Add amount in destination
            // 5. save both

            sourceAccount.setBalance(sourceAccount.getBalance() - transferFundModel.getTransferAmount());
            desinationAccount.setBalance(desinationAccount.getBalance() + transferFundModel.getTransferAmount());

            userService.addUser(sourceAccount);
            userService.addUser(desinationAccount);

            applicationMessage.setSuccess(true);
            applicationMessage.setBody(null);
            applicationMessage.setMessage("Transferred Successfully");

        }

        return applicationMessage;
    }

    @GetMapping(value = "delete")
    public String showDeleteForm(){

        return "delete";

    }

    @DeleteMapping(value="/delete/{accountNumber}")
    @ResponseBody
    public ResponseEntity<?> processCloseAccount(@PathVariable Long accountNumber) {
        userService.deleteUserByAccountNumber(accountNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
