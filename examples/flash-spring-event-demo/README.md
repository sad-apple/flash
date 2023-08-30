# spring event 使用监听者模式对代码进行解耦

## 快速开始

- 定义事件

  ``` java
  public class DemoEvent extends ApplicationEvent {
  
      private String name;
  
      public DemoEvent(String name) {
          super(name);
          this.name = name;
      }
  
      public String getName() {
          return name;
      }
  
  }
  ```


- 定义事件发布者

  事件发布可以用多种方式，这里通过实现ApplicationEventPublisherAware自定义一个发布者，这样实现更灵活一点

  ``` java
  @Component
  public class DemoPusher implements ApplicationEventPublisherAware {
  
      ApplicationEventPublisher applicationEventPublisher;
      @Override
      public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
          this.applicationEventPublisher = applicationEventPublisher;
      }
  
      public void success(String name) {
          applicationEventPublisher.publishEvent(new DemoEvent(name));
      }
  }
  ```

- 定义事件监听者

  监听者的实现也是可以通过实现接口实现，这里使用的是注解的方式
  
  ``` java
  @Component
  public class DemoListener {
  
      @EventListener
      public void listener1(DemoEvent event) {
          System.out.println("listener1:" + event.getName());
      }
  
      @Async
      @EventListener
      public void listener2(DemoEvent event) {
          System.out.println("listener2:" + event.getName());
      }
  
      @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
      public void listener3(DemoEvent event) {
          System.out.println("listener3:" + event.getName());
      }
  
  }
  ```


- 发送事件

  ``` java
  @RequestMapping("/event")
  @RestController
  public class EventController {
  
      @Autowired
      private DemoPusher demoPusher;
  
      @GetMapping("/success")
      @Transactional()
      public void success(String name) {
          Student student = new Student();
          student.setRealname(name);
          student.setAge(12);
          student.setIdCard("341181199110054813");
          student.insert();
  //        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          demoPusher.success(name);
          System.out.println("事务提交了吗");
      }
  
  }
  ```




## 注意事项

- 监听者本质也是同步执行，并不是异步执行，如果需要异步执行，需要在监听方法上加上@Async注解
- spring事件机制提供了注解@TransactionalEventListener，这个注解是在@EventListener注解基础上实现了对监听方法事务的判断：
  - phase字段用来判断监听方法事务所在的阶段
  - fallbackExecution字段用来判断在没有事务的情况下是否执行