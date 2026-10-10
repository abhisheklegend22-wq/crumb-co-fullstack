package com.crumbco.bakery.dto;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import java.util.List;
public record OrderRequest(@NotBlank @Size(max=120) String customerName,@NotBlank @Email @Size(max=160) String email,
 @NotBlank @Pattern(regexp="[0-9+() -]{7,20}") String phone,@NotBlank @Size(max=500) String address,
 @NotBlank @Size(max=80) String city,@NotEmpty @Valid List<OrderLineRequest> items) {
 public record OrderLineRequest(@NotNull @Positive Long productId,@NotNull @Min(1) @Max(100) Integer quantity) {}
}
