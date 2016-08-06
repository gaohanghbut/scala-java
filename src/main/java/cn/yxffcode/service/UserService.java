package cn.yxffcode.service;

import cn.yxffcode.dao.UserDao;
import cn.yxffcode.model.User;
import org.springframework.stereotype.Service;
import scala.Option;

import javax.annotation.Resource;

/**
 * @author gaohang on 5/31/16.
 */
@Service
public class UserService {

  @Resource
  private UserDao userDao;

  public void createUser(User user) {
    userDao.insert(user);
  }

  public User getUser(int userId) {
    Option<User> userOption = userDao.findById(userId);
    return userOption.isDefined() ? userOption.get() : null;
  }
}
