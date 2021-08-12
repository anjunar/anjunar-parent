package de.bitvale.common.rest;

import de.bitvale.common.rest.api.Link;
import de.bitvale.common.security.Identity;
import de.bitvale.introspector.bean.BeanIntrospector;
import de.bitvale.introspector.bean.BeanModel;
import de.bitvale.introspector.bean.BeanProperty;
import de.bitvale.introspector.type.TypeResolver;
import de.bitvale.introspector.type.resolved.ResolvedAnnotatedElement;
import de.bitvale.introspector.type.resolved.ResolvedMethod;
import de.bitvale.introspector.type.resolved.ResolvedParameter;
import de.bitvale.introspector.type.resolved.ResolvedType;
import de.bitvale.jsr339.ResourceUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author by Patrick Bittner on 11.06.15.
 */
public class URLBuilder<B> {

    private static final Logger log = LoggerFactory.getLogger(URLBuilder.class);

    private final Identity identity;

    private final EntityManager entityManager;

    private final UriBuilder uriBuilder;

    private final ParamConverterProvider converterProvider;

    private URLBuilder<?> child;

    private B instance;

    private ResolvedMethod<?> method;

    private Object[] args;

    private String httpMethod;

    private String rel;

    private Map<String, Object> params = new HashMap<>();

    public URLBuilder(final Class<B> aClass, final UriBuilder uriBuilder, Identity identity, EntityManager entityManager, ParamConverterProvider converterProvider) {
        this.uriBuilder = uriBuilder;
        this.identity = identity;
        this.entityManager = entityManager;
        this.converterProvider = converterProvider;

        ResolvedType<B> type = TypeResolver.resolve(aClass);

        final Path classPath = type.getAnnotation(Path.class);

        instance = (B) Enhancer.create(aClass, (MethodInterceptor) (o, thisMethod, args, methodProxy) -> {
            final ResolvedMethod<B> resolvedMethod = type.getMethods()
                    .stream()
                    .filter(method -> method.equalSignature(thisMethod))
                    .findFirst()
                    .get();

            this.method = resolvedMethod;
            this.args = args;
            if (this.rel == null) {
                this.rel = resolvedMethod.getName();
            }

            Path methodPath = resolvedMethod.getAnnotation(Path.class);

            if (classPath != null && methodPath != null) {
                uriBuilder.path(aClass);
                uriBuilder.path(thisMethod);
            }

            if (classPath == null && methodPath != null) {
                uriBuilder.path(thisMethod);
            }

            if (classPath != null && methodPath == null) {
                uriBuilder.path(aClass);
            }

            httpMethod = ResourceUtil.httpMethod(resolvedMethod);

            for (int index = 0; index < args.length; index++) {
                Object arg = args[index];
                ResolvedParameter<B> parameter = resolvedMethod.getParameters().get(index);

                readParameter(arg, parameter);

                BeanParam beanParam = parameter.getAnnotation(BeanParam.class);
                if (beanParam != null && arg != null) {

                    BeanModel<?> beanModel = BeanIntrospector.create(parameter.getType());

                    for (BeanProperty<?, ?> beanProperty : beanModel.getProperties()) {

                        readParameter(((BeanProperty<Object, ?>) beanProperty).apply(arg), beanProperty);

                    }

                }

            }

            if (httpMethod == null) {
                child = new URLBuilder<>(resolvedMethod.getReturnType().getRawType(), uriBuilder, identity, entityManager, converterProvider);
                return child.instance();
            } else {
                return null;
            }

        });
    }

    private void readParameter(Object arg, ResolvedAnnotatedElement parameter) {
        if (arg != null) {
            ParamConverter converter = converterProvider.getConverter(arg.getClass(), null, parameter.getAnnotations());
            if (converter != null) {
                arg = converter.toString(arg);
            }
        }

        PathParam pathParam = parameter.getAnnotation(PathParam.class);
        if (pathParam != null) {
            params.put(pathParam.value(), arg);
        }

        QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
        if (queryParam != null && arg != null) {
            uriBuilder.queryParam(queryParam.value(), arg);
        }

        MatrixParam matrixParam = parameter.getAnnotation(MatrixParam.class);
        if (matrixParam != null && arg != null) {
            uriBuilder.matrixParam(matrixParam.value(), arg);
        }
    }

    private B instance() {
        return instance;
    }

    private String buildMethod() {
        if (httpMethod != null) {
            return httpMethod;
        }

        return child.buildMethod();
    }

    public URLBuilder<B> record(Consumer<B> consumer) {
        consumer.accept(instance);
        return this;
    }

    public URLBuilder<B> rel(String rel) {
        this.rel = rel;
        return this;
    }

    public boolean hasRoles(String[] roles) {
        for (String role : roles) {
            if (identity.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    public void build(Consumer<B> record, Consumer<Link> build) {

    }

    public void build(Consumer<Link> consumer) {

        URI url = uriBuilder.buildFromMap(params);

        RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);

        if (rolesAllowed == null) {

            consumer.accept(new Link("service" + url.toASCIIString(), buildMethod(), rel));

        } else {
            if (hasRoles(rolesAllowed.value())) {

                MethodPredicate methodPredicate = method.getAnnotation(MethodPredicate.class);

                if (methodPredicate == null) {
                    consumer.accept(new Link("service" + url.toASCIIString(), buildMethod(), rel));
                } else {

                    Class<?> resolverCLass = methodPredicate.value();

                    try {
                        Constructor<?> constructor = resolverCLass.getDeclaredConstructor(Identity.class, EntityManager.class);
                        Object resolverInstance = constructor.newInstance(identity, entityManager);

                        ResolvedType<?> resolvedType = TypeResolver.resolve(resolverCLass);
                        ResolvedMethod<Object> resolvedMethod = (ResolvedMethod<Object>) resolvedType.getMethods().get(0);

                        boolean result = (boolean) resolvedMethod.invoke(resolverInstance, args[0]);

                        if (result) {
                            consumer.accept(new Link("service" + url.toASCIIString(), buildMethod(), rel));
                        }

                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        log.error(e.getMessage());
                    }

                }

            }
        }

    }

}
