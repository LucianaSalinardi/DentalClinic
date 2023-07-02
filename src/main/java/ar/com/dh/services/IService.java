package ar.com.dh.services;

import java.util.List;

public interface IService<T, E> {

    public List<T> getAll();

    public T getOne(Long id);

    public void create(E e);

    public void update(E e);

    public void delete(Long id);


}
