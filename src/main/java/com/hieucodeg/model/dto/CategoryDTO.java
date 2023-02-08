package com.hieucodeg.model.dto;

import com.hieucodeg.model.Category;
import com.hieucodeg.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @NotNull(message = "Thể loại là bắt buộc")
    @Pattern(regexp = "^\\d+$", message = "ID thể loại phải là số")
    private String id;

    private String name;

    public Category toCategory() {
        return new Category()
                .setId(Long.valueOf(id))
                .setName(name);
    }
}
