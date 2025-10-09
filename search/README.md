# Description

This module is responsible search screen and logic around it

# How to install

1. Copy module folder to your project and add module to gradle dependency like
   this:

```groovy
implementation project(':features:search')
```

2. Module requires next modules to work

```groovy
implementation project(':libs:core')
implementation project(':libs:ui_base')
```

3. Add next nav graphs to main navigation graph

```xml
<include app:graph="@navigation/nav_search" />
<include app:graph="@navigation/nav_search_bullet" />
```

4. The following actions should be added into the root navigation graph

 ```xml

<action 
    android:id="@+id/action_awardDeliveryHomeF_to_destination_searchF"
    app:destination="@id/nav_search">
    <argument 
        android:name="key" 
        app:argType="string" />
    <argument 
        android:name="searchableList" 
        android:defaultValue="@null"
        app:argType="ua.gov.diia.search.models.SearchableItem[]"
        app:nullable="true" />
</action>
```
