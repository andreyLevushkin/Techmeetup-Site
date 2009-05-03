Tapestry.Initializer.autocompleter = function(elementId, menuId, url, config)
{
    $T(elementId).autocompleter = new Ajax.Autocompleter(elementId, menuId, url, config);
};