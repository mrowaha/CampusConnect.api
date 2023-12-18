package com.campusconnect.domain.user.entity;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.common.converter.ArrayListToJsonConverter;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.transaction.entity.Bid;
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

    // Trust score of the Bilkenteer, with a precision of 3 digits and a scale of 1
    @Column(name = "trust_score", precision = 3, scale = 1)
    private Integer trustScore = 4;

    // Flag indicating whether the Bilkenteer is suspended or not
    @Column(name = "is_suspended")
    private Boolean isSuspended = false;

    // List of phone numbers associated with the Bilkenteer, stored as ArrayList
    @Column(name = "phone_number", nullable = true)
    @Convert(converter = ArrayListToJsonConverter.class )
    private ArrayList<String> phoneNumbers = new ArrayList<>();

    // Address information of the Bilkenteer, stored using a custom converter
    @Column(name = "address")
    @Convert(converter =  BilkenteerAddressConverter.class)
    private BilkenteerAddress address;

    // List of products associated with the Bilkenteer, where the Bilkenteer is the seller
    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "seller_id")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    // Set of forum posts created by the Bilkenteer
    @OneToMany(mappedBy = "postingUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ForumPost> forumPosts = new HashSet<>();

    // Set of product tags that the Bilkenteer is subscribed to
    @OneToMany(fetch = FetchType.LAZY)
    private Set<ProductTag> subscribedTags = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference(value = "bider_id")
    @JsonIgnore
    private Set<Bid> bids = new HashSet<>();

    // Overrides the method in User class to provide Bilkenteer-specific authorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = Role.BILKENTEER::name;
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    // Overrides the method in User class to indicate whether the Bilkenteer is enabled or not (suspended)
    @Override
    public boolean isEnabled() {
        return !this.isSuspended;
    }

    // Overrides the default toString method
    @Override
    public String toString() {
        return "";
    }
}
