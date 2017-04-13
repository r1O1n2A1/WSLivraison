(function() {
    'use strict';
    angular
        .module('wsLivraisonApp')
        .factory('ShippingMethod', ShippingMethod);

    ShippingMethod.$inject = ['$resource'];

    function ShippingMethod ($resource) {
        var resourceUrl =  'api/shipping-methods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
