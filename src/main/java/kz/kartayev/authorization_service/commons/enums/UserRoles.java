package kz.kartayev.authorization_service.commons.enums;

public enum UserRoles {
  /**
  Может назначить/заблокировать админов, и пользователей. Может менять пароль пользователям
   */
  SUPER_ADMIN,

  /**
   Блокировка пользователей.
  */
  ADMIN,

  /**
   * Доступ к ресурсу. Покупатель
   * */
  USER,

  /**
   * Доступ в создании товаров.
   * */
  CUSTOMER;
}
