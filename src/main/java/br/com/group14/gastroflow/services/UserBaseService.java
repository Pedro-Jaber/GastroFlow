package br.com.group14.gastroflow.services;

import br.com.group14.gastroflow.entities.user.UserBase;
import br.com.group14.gastroflow.repositories.UserBaseRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class UserBaseService<T extends UserBase> {

    protected abstract UserBaseRepository getUserBaseRepository();

}
