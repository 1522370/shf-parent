package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2022-10-11  14:43
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //指定一个内存中的静态的账号密码
        auth.inMemoryAuthentication()
                .withUser("atguigu")
                .password(passwordEncoder.encode("123456"))
                .roles("");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许页面嵌套
        http.headers().frameOptions().disable();
        http
            .authorizeRequests()
            .antMatchers("/static/**","/login").permitAll()  //哪些请求不需要认证就能访问
            .anyRequest().authenticated()    // 哪些请求需要认证才能访问
            .and()
            .formLogin()
            .loginPage("/login")    //指定哪个资源作为登录页面
            .defaultSuccessUrl("/") //指定登录成功之后跳转到哪个资源
            .and()
            .logout()
            .logoutUrl("/logout")   //退出登陆的路径，指定spring security拦截的注销url,退出功能是security提供的
            .logoutSuccessUrl("/login");//用户退出后要被重定向的url

        //关闭跨域请求伪造
        http.csrf().disable();

        //当权限不够时直接显示异常页面
        //http.exceptionHandling().accessDeniedPage("/auth");

        //当权限不够时,可以先通过代码进行一些你想要的处理,然后再显示异常页面
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }
    @Bean
    public PasswordEncoder createPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
