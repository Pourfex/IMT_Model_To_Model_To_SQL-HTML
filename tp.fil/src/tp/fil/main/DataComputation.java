package tp.fil.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import org.eclipse.modisco.java.Model;
import org.eclipse.modisco.java.NamedElement;
import org.eclipse.modisco.java.Package;
import org.eclipse.modisco.java.TypeAccess;
import org.eclipse.modisco.java.VariableDeclarationFragment;
import org.eclipse.modisco.java.VisibilityKind;
import org.eclipse.modisco.java.emf.JavaPackage;
import org.eclipse.modisco.java.emf.impl.ClassDeclarationImpl;
import org.eclipse.modisco.java.emf.impl.ModelImpl;
import org.eclipse.modisco.java.AbstractTypeDeclaration;
import org.eclipse.modisco.java.BodyDeclaration;
import org.eclipse.modisco.java.ClassDeclaration;
import org.eclipse.modisco.java.FieldDeclaration;


public class DataComputation {
	
	public static void main(String[] args) {
		try {
			Resource javaModel;
			Resource dataModel;
			Resource dataMetamodel;
			
			ResourceSet resSet = new ResourceSetImpl();
			resSet.getResourceFactoryRegistry().
				getExtensionToFactoryMap().
				put("ecore", new EcoreResourceFactoryImpl());
			resSet.getResourceFactoryRegistry().
				getExtensionToFactoryMap().
				put("xmi", new XMIResourceFactoryImpl());
			
			JavaPackage.eINSTANCE.eClass();
			
			dataMetamodel = resSet.createResource(URI.createFileURI("src/tp/fil/resources/Data.ecore"));
			dataMetamodel.load(null);
			EPackage.Registry.INSTANCE.
				put("http://data", dataMetamodel.getContents().get(0));
			
			dataModel = resSet.createResource(URI.createFileURI(args[0]));
			dataModel.load(null);
			
			
			EPackage packageContent = (EPackage) dataMetamodel.getContents().get(0);
			EClass modelClassifier = (EClass) packageContent.getEClassifier("Model");
			EClass classClassifier = (EClass) packageContent.getEClassifier("Class");
			EClass attributeClassifier = (EClass) packageContent.getEClassifier("Attribute");
			EClass typeClassifier = (EClass) packageContent.getEClassifier("Type");
			

			TreeIterator<EObject> iterator = dataModel.getAllContents();
			
			List<Package> packages = iteratorToList(dataModel.getAllContents())
					.stream().filter(Package.class::isInstance)
					.map(obj -> (Package) obj)
					.filter(obj -> obj.getName().equals("model"))
					.collect(Collectors.toList());
			
			
			Package packageItem = packages.get(0);
			List<EObject> classes = new ArrayList<>();
			
			iteratorToList(packageItem.eAllContents())
					.stream()
					.filter(ClassDeclaration.class::isInstance)
					.map(classObj -> {
				
				List<EObject> fields = new ArrayList<>();	
				iteratorToList(classObj.eAllContents())
						.stream().filter(FieldDeclaration.class::isInstance)
						
						.map(fieldObj -> {
							FieldDeclaration newFieldObj = (FieldDeclaration) fieldObj;
							
							EObject newAttributeObj = packageContent.getEFactoryInstance().create(attributeClassifier);
							
							
							// Create type
							EObject newTypeObj = packageContent.getEFactoryInstance().create(typeClassifier);
							newTypeObj.eSet(typeClassifier.getEStructuralFeature("name"), newFieldObj.getType().getType().getName());
							
							boolean isRef = !newFieldObj.getType().getType().getName().matches("String|int|float|double");
							newTypeObj.eSet(typeClassifier.getEStructuralFeature("isReference"), isRef);
							
							newAttributeObj.eSet(attributeClassifier.getEStructuralFeature("name"), newFieldObj.getFragments().get(0).getName());
							newAttributeObj.eSet(attributeClassifier.getEStructuralFeature("type"), newTypeObj);
							newAttributeObj.eSet(attributeClassifier.getEStructuralFeature("visibility"), newFieldObj.getModifier().getVisibility().getLiteral());
					
							fields.add(newAttributeObj);
							return fieldObj;
						}).collect(Collectors.toList());
				
				ClassDeclaration cDeclar = (ClassDeclaration) classObj;
				EObject newAttributeClass = packageContent.getEFactoryInstance().create(classClassifier);
				newAttributeClass.eSet(classClassifier.getEStructuralFeature("name"), cDeclar.getName());
				
				newAttributeClass.eSet(classClassifier.getEStructuralFeature("attributes"), fields);
				classes.add(newAttributeClass);
				
				return classObj;
			}).collect(Collectors.toList());
			
			
			EObject newModel = packageContent.getEFactoryInstance().create(modelClassifier);
			newModel.eSet(modelClassifier.getEStructuralFeature("name"), packageItem.getName());
			newModel.eSet(modelClassifier.getEStructuralFeature("classes"), classes);
			
			dataModel.getContents().clear();
			dataModel.getContents().add(newModel);
				
			dataModel.save(new FileOutputStream("javaproject.xmi"), null);
			dataModel.unload();
			dataMetamodel.unload();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static <T> List<T> iteratorToList(Iterator<T> iterator){
		List<T> list = new ArrayList<>();
		iterator.forEachRemaining(e -> list.add(e));
		return list;
	}

}