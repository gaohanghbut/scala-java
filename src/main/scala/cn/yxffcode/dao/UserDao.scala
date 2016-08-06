package cn.yxffcode.dao

import cn.yxffcode.model.User

/**
  * Created by gaohang on 5/31/16.
  */
trait UserDao {

  /**
    * insert a user into the db
    *
    * @param user
    */
  def insert(user: User);

  /**
    * select a user specified by id
    *
    * @param id user id
    * @return user
    */
  def findById(id: Int): Option[User];
}
