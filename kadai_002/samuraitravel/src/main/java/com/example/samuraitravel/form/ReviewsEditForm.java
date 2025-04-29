package com.example.samuraitravel.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewsEditForm {


	@Min(1)
	@Max(5)
	@NotNull(message = "評価を選択してください。")
	private int star;

	@Size(max = 255, message = "コメントは255文字以内で入力してください。")
	@NotBlank(message = "コメントを入力してください。")
	private String comment;


}
