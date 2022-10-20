package ru.rusguardian.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.rusguardian.service.data.exception.ClassTypeException;
import ru.rusguardian.service.data.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
public abstract class CrudService<T> {

    /**
     * Property - simpleClassName for logs and table names in SQL
     */
    private final String simpleClassName;

    /**
     * Property - genericType for Object casting
     */
    private final Class<T> genericType;

    @Autowired
    private CrudRepository<T, Long> crudRepository;

    protected CrudService() {
        Class<?> classType = GenericTypeResolver.resolveTypeArgument(getClass(), CrudService.class);
        this.genericType = (Class<T>) classType;
        if (genericType == null) {
            throw new ClassTypeException("Generic type in CrudService is null");
        }
        this.simpleClassName = this.genericType.getSimpleName();
    }

    public T findById(Long id) {
        log.debug("Searching {} by id = {}", simpleClassName, id);
        Optional<T> entity = crudRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Cannot find entity by id");
        }
        return entity.get();
    }

    public T create(T entity) {
        log.debug("Creating {}", simpleClassName);
        return crudRepository.save(entity);
    }

    public List<T> getAll() {
        log.debug("Getting all {}", simpleClassName);
        List<T> entities = new ArrayList<>();
        crudRepository.findAll().forEach(entities::add);
        return entities;
    }
}
