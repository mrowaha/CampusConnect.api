package com.campusconnect.domain.user.entity;

import com.campusconnect.domain.comment.entity.Comment;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.user.converter.BilkenteerAddressConverter;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.domain.user.pojo.BilkenteerAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="cc_bilkenteer")
@RequiredArgsConstructor
@SuperBuilder
public class Bilkenteer extends User {

    @Column(name = "trust_score", precision = 3, scale = 1)
    private Integer trustScore = 4;

    @Column(name = "is_suspended")
    private Boolean isSuspended = false;

    @ElementCollection(targetClass =  String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "phone_numbers", joinColumns = @JoinColumn(name = "bilkenteer_id"))
    @Column(name = "phone_number", nullable = false)
    private List<String> phoneNumbers = new ArrayList<>();

    @Column(name = "address")
    @Convert(converter =  BilkenteerAddressConverter.class)
    private BilkenteerAddress address;

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "seller_id")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "postingUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ForumPost> forumPosts = new HashSet<>();

//    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL)
//    private Set<Comment> comments = new HashSet();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = Role.BILKENTEER::name;
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return !this.isSuspended;
    }

    @Override
    public String toString() {
        return "";
    }
}
