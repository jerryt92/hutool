package cn.hutool.core.annotation;

import cn.hutool.core.util.ObjUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.Map;

/**
 * test for {@link AnnotationUtil}
 */
public class AnnotationUtilTest {

	@Test
	public void testToCombination() {
		CombinationAnnotationElement element = AnnotationUtil.toCombination(ClassForTest.class);
		Assert.assertEquals(2, element.getAnnotations().length);
	}

	@Test
	public void testGetAnnotations() {
		Annotation[] annotations = AnnotationUtil.getAnnotations(ClassForTest.class, true);
		Assert.assertEquals(2, annotations.length);
		annotations = AnnotationUtil.getAnnotations(ClassForTest.class, false);
		Assert.assertEquals(1, annotations.length);
	}

	@Test
	public void testGetCombinationAnnotations() {
		MetaAnnotationForTest[] annotations = AnnotationUtil.getCombinationAnnotations(ClassForTest.class, MetaAnnotationForTest.class);
		Assert.assertEquals(1, annotations.length);
	}

	@Test
	public void testAnnotations() {
		MetaAnnotationForTest[] annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, false, MetaAnnotationForTest.class);
		Assert.assertEquals(0, annotations1.length);
		annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, true, MetaAnnotationForTest.class);
		Assert.assertEquals(1, annotations1.length);

		Annotation[] annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, false, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		Assert.assertEquals(0, annotations2.length);
		annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, true, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		Assert.assertEquals(1, annotations2.length);
	}

	@Test
	public void testGetAnnotation() {
		MetaAnnotationForTest annotation = AnnotationUtil.getAnnotation(ClassForTest.class, MetaAnnotationForTest.class);
		Assert.assertNotNull(annotation);
	}

	@Test
	public void testHasAnnotation() {
		Assert.assertTrue(AnnotationUtil.hasAnnotation(ClassForTest.class, MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotationValue() {
		AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		Assert.assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class));
		Assert.assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "value"));
		Assert.assertNull(AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "property"));
	}

	@Test
	public void testGetAnnotationValueMap() {
		AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		Map<String, Object> valueMap = AnnotationUtil.getAnnotationValueMap(ClassForTest.class, AnnotationForTest.class);
		Assert.assertNotNull(valueMap);
		Assert.assertEquals(1, valueMap.size());
		Assert.assertEquals(annotation.value(), valueMap.get("value"));
	}

	@Test
	public void testGetRetentionPolicy() {
		RetentionPolicy policy = AnnotationForTest.class.getAnnotation(Retention.class).value();
		Assert.assertEquals(policy, AnnotationUtil.getRetentionPolicy(AnnotationForTest.class));
	}

	@Test
	public void testGetTargetType() {
		ElementType[] types = AnnotationForTest.class.getAnnotation(Target.class).value();
		Assert.assertArrayEquals(types, AnnotationUtil.getTargetType(AnnotationForTest.class));
	}

	@Test
	public void testIsDocumented() {
		Assert.assertFalse(AnnotationUtil.isDocumented(AnnotationForTest.class));
	}

	@Test
	public void testIsInherited() {
		Assert.assertFalse(AnnotationUtil.isInherited(AnnotationForTest.class));
	}

	@Test
	public void testSetValue() {
		AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		String newValue = "is a new value";
		Assert.assertNotEquals(newValue, annotation.value());
		AnnotationUtil.setValue(annotation, "value", newValue);
		Assert.assertEquals(newValue, annotation.value());
	}

	@Test
	public void testGetAnnotationAlias() {
		MetaAnnotationForTest annotation = AnnotationUtil.getAnnotationAlias(AnnotationForTest.class, MetaAnnotationForTest.class);
		Assert.assertEquals(annotation.value(), annotation.alias());
		Assert.assertEquals(MetaAnnotationForTest.class, annotation.annotationType());
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface MetaAnnotationForTest{
		@Alias(value = "alias")
		String value() default "";
		String alias() default "";
	}

	@MetaAnnotationForTest("foo")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface AnnotationForTest{
		String value() default "";
	}

	@AnnotationForTest("foo")
	private static class ClassForTest{}

}
