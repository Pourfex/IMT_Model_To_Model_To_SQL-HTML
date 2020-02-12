package tp.fil.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
			
			dataModel = resSet.createResource(URI.createFileURI("PetStore_java.xmi"));
			dataModel.load(null);
			
			
			EPackage packageContent = (EPackage) dataMetamodel.getContents().get(0);
			EClass modelClassifier = (EClass) packageContent.getEClassifier("Model");
			EClass classClassifier = (EClass) packageContent.getEClassifier("Class");
			EClass attributeClassifier = (EClass) packageContent.getEClassifier("Attribute");
			EClass typeClassifier = (EClass) packageContent.getEClassifier("Type");
			

			TreeIterator<EObject> classIterator = dataModel.getAllContents();

				while(classIterator.hasNext()) {
					EObject classElement = classIterator.next();
					
					if (classElement.eClass().getName().equals("ClassDeclaration")) {
						EObject newClassObj = packageContent.getEFactoryInstance().create(classClassifier);
						
						// Set class name
						newClassObj.eSet(classClassifier.getEStructuralFeature("name"), classElement.eGet(classElement.eClass().getEStructuralFeature("name")));
						
						
						// Get class attributes
						TreeIterator<EObject> attributeIterator = classElement.eAllContents();
						while(attributeIterator.hasNext()) {
							EObject attributeElement = attributeIterator.next();
							
							if(attributeElement.eClass().getName().equals("FieldDeclaration")) {
								
								EObject newAttributeObj = packageContent.getEFactoryInstance().create(attributeClassifier);
								
								EObject newTypeObj = packageContent.getEFactoryInstance().create(typeClassifier);
								newTypeObj.eSet(typeClassifier.getEStructuralFeature("name"), typeClassifier.eGet(attributeElement.eClass().getEStructuralFeature("type")));
								
								newAttributeObj.eSet(attributeClassifier.getEStructuralFeature("name"), attributeClassifier.eGet(attributeElement.eClass().getEStructuralFeature("name")));
								newAttributeObj.eSet(attributeClassifier.getEStructuralFeature("type"), newTypeObj);
								newAttributeObj.eSet(attributeClassifier.getEStructuralFeature("visibility"), attributeClassifier.eGet(attributeElement.eClass().getEStructuralFeature("visibility")));
								
								// Add field to class
								List<EObject> attributes = (List<EObject>) classClassifier.eGet(classClassifier.eClass().getEStructuralFeature("attributes"));
								attributes.add(newAttributeObj);
								newClassObj.eSet(classClassifier.getEStructuralFeature("attributes"), attributes);
							}
						}
						
						dataModel.getContents().add(newClassObj);
					}
						
				}
						
			dataModel.save(new FileOutputStream("PetStore_java_data.xmi"), null);
			dataModel.unload();
			dataMetamodel.unload();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}